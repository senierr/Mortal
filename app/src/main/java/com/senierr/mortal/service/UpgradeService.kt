package com.senierr.mortal.service

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.*
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.notification.NotificationManager
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.service.api.ICommonService
import com.senierr.repository.service.api.ISettingService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.File

/**
 * 后台升级服务
 *
 * @author chunjiezhou
 * @date 2020/12/29
 */
class UpgradeService : Service(), LifecycleOwner, CoroutineScope by MainScope() {

    companion object {
        private const val TAG_LOG = "UpgradeService"
        private const val TAG_DOWNLOAD = "upgrade_service_apk_download"
    }

    private val lifecycleRegistry = LifecycleRegistry(this)

    private val settingService = Repository.getService<ISettingService>()
    private val commonService = Repository.getService<ICommonService>()

    private var upgradeCallback: UpgradeCallback? = null

    override fun onCreate() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        super.onCreate()
        Repository.getProgressBus().downloadProgress.observe(this, Observer {
            if (it.tag == TAG_DOWNLOAD) {
                NotificationManager.sendUpdateNotification(this, it.percent)
            }
        })
    }

    override fun onBind(intent: Intent?): IBinder {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        return UpgradeBinder()
    }

    override fun onUnbind(intent: Intent?): Boolean {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        // 注意解除引用，防止内存泄漏
        upgradeCallback = null
        cancel()
        super.onDestroy()
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry

    /**
     * 检查新版本
     */
    fun checkNewVersion() {
        launch {
            try {
                val versionInfo = settingService.checkNewVersion()
                versionInfo?.let {
                    upgradeCallback?.onNewVersion(it)
                }
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
                settingService.ignoreUpdateVersion(versionInfo.versionName)
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
                val apkFile = commonService.downloadFile(
                    TAG_DOWNLOAD,
                    versionInfo.url,
                    versionInfo.fileName,
                    versionInfo.md5
                )
                // 移除下载通知
                NotificationManager.cancel(applicationContext, NotificationManager.NOTIFY_ID_UPDATE)
                // 通知调用者
                upgradeCallback?.onDownloadCompleted(apkFile)
            } catch (e: Exception) {
                LogUtil.logE(TAG_LOG, Log.getStackTraceString(e))
            }
        }
    }

    inner class UpgradeBinder : Binder() {
        fun getService(): UpgradeService = this@UpgradeService
        fun setCallBack(callback: UpgradeCallback) {
            this@UpgradeService.upgradeCallback = callback
        }
    }

    interface UpgradeCallback {
        fun onNewVersion(versionInfo: VersionInfo)
        fun onDownloadCompleted(apkFile: File)
    }
}