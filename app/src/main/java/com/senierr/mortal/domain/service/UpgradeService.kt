package com.senierr.mortal.domain.service

import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.*
import com.google.gson.Gson
import com.senierr.mortal.support.utils.LogUtil
import com.senierr.mortal.domain.notification.NotificationManager
import com.senierr.mortal.repository.Repository
import com.senierr.mortal.repository.entity.bmob.VersionInfo
import com.senierr.mortal.repository.store.disk.DiskManager
import com.senierr.mortal.repository.store.remote.RemoteManager
import com.senierr.mortal.repository.store.remote.api.CommonApi
import com.senierr.mortal.repository.store.remote.api.SettingApi
import com.senierr.mortal.repository.store.sp.SPKey
import com.senierr.mortal.repository.store.sp.SPManager
import com.senierr.mortal.support.utils.AppUtil
import com.senierr.mortal.support.utils.FileUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.io.File
import kotlin.coroutines.resumeWithException

/**
 * 后台升级服务
 *
 * @author chunjiezhou
 * @date 2020/12/29
 */
class UpgradeService : LifecycleService() {

    companion object {
        private const val TAG_LOG = "UpgradeService"
        private const val TAG_DOWNLOAD = "upgrade_service_apk_download"
    }

    inner class UpgradeBinder : Binder() {
        fun getService(): UpgradeService = this@UpgradeService
        fun setCallBack(callback: UpgradeCallback) {
            this@UpgradeService.upgradeCallback = callback
        }
    }

    interface UpgradeCallback {
        fun onNewVersion(versionInfo: VersionInfo?)
        fun onDownloadCompleted(apkFile: File)
    }

    private val settingApi by lazy { RemoteManager.getBmobHttp().create(SettingApi::class.java) }
    private val commonApi by lazy { RemoteManager.getGankHttp().create(CommonApi::class.java) }
    private val spUtil by lazy { SPManager.getSP() }

    private var upgradeCallback: UpgradeCallback? = null

    override fun onCreate() {
        super.onCreate()
        Repository.getProgressBus().downloadProgress.observe(this, Observer {
            if (it.tag == TAG_DOWNLOAD) {
                NotificationManager.sendUpdateNotification(this, it.percent)
            }
        })
    }

    override fun onBind(intent: Intent?): IBinder {
        return UpgradeBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        // 注意解除引用，防止内存泄漏
        upgradeCallback = null
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        // 注意解除引用，防止内存泄漏
        upgradeCallback = null
        super.onDestroy()
    }

    /**
     * 检查新版本
     */
    fun checkNewVersion() {
        launch {
            try {
                val versionInfo = withContext(Dispatchers.IO) {
                    val where = HashMap<String, String>()
                    where["applicationId"] = Repository.getApplication().packageName
                    where["platform"] = "android"
                    val newVersionInfo = settingApi.checkNewVersion(Gson().toJson(where)).results.firstOrNull()
                    if (newVersionInfo != null) {
                        // 判断是否是新版本
                        if (checkIfNewVersion(newVersionInfo.versionName)) {
                            // 判断是否是忽略版本
                            val ignoreVersionName = spUtil.getString(SPKey.IGNORE_UPDATE_VERSION_NAME)
                            if (newVersionInfo.versionName != ignoreVersionName) {
                                return@withContext newVersionInfo
                            }
                        }
                    }
                    return@withContext null
                }
                upgradeCallback?.onNewVersion(versionInfo)
            } catch (e: Exception) {
                LogUtil.logE(TAG_LOG, Log.getStackTraceString(e))
            }
        }
    }

    /**
     * 忽略此版本
     */
    fun ignoreThisVersion(versionInfo: VersionInfo) {
        launch {
            try {
                withContext(Dispatchers.IO) {
                    spUtil.putString(SPKey.IGNORE_UPDATE_VERSION_NAME, versionInfo.versionName)
                    return@withContext true
                }
            } catch (e: Exception) {
                // ignore
            }
        }
    }

    /**
     * 下载APK
     */
    fun downloadApk(versionInfo: VersionInfo) {
        launch {
            try {
                val apkFile = withContext(Dispatchers.IO) {
                    suspendCancellableCoroutine<File> { continuation ->
                        try {
                            val call = commonApi.downloadFile(versionInfo.url, TAG_DOWNLOAD)
                            continuation.invokeOnCancellation {
                                call.cancel()
                            }
                            FileUtil.saveFile(call, DiskManager.getDownloadDir(), versionInfo.fileName, versionInfo.md5)
                        } catch (e: Exception) {
                            continuation.resumeWithException(e)
                        }
                    }
                }
                // 移除下载通知
                NotificationManager.cancel(applicationContext, NotificationManager.NOTIFY_ID_UPDATE)
                // 通知调用者
                upgradeCallback?.onDownloadCompleted(apkFile)
            } catch (e: Exception) {
                LogUtil.logE(TAG_LOG, Log.getStackTraceString(e))
            }
        }
    }

    /**
     * 检测是否需要升级
     */
    private fun checkIfNewVersion(targetVersionName: String): Boolean {
        val currentVersionName = AppUtil.getVersionName(Repository.getApplication()) ?: return false
        val currentVersionArray = currentVersionName.split(".").toTypedArray()
        val targetVersionArray = targetVersionName.split(".").toTypedArray()
        val minLength = currentVersionArray.size.coerceAtMost(targetVersionArray.size)
        for (i in 0 until minLength) {
            val diff = targetVersionArray[i].toInt() - currentVersionArray[i].toInt()
            if (diff != 0) return diff > 0
        }
        return false
    }
}