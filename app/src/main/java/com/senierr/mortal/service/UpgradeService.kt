package com.senierr.mortal.service

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.Observer
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.receiver.UpgradeReceiver
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.VersionInfo
import com.senierr.repository.service.api.ICommonService
import com.senierr.repository.service.api.ISettingService
import kotlinx.coroutines.launch

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

        const val KEY_ACTION = "key_action"
        const val ACTION_CHECK_NEW_VERSION = "action_check_new_version"
        const val ACTION_IGNORE_THIS_VERSION = "action_ignore_this_version"
        const val ACTION_DOWNLOAD_APK = "action_download_apk"

        const val ACTION_NEW_VERSION = "action_new_version"
        const val ACTION_DOWNLOAD_PROGRESS = "action_download_progress"
        const val ACTION_DOWNLOAD_COMPLETED = "action_download_completed"
        const val KEY_VERSION_INFO = "key_version_info"
        const val KEY_FILE_APK = "key_file_apk"
        const val KEY_DOWNLOAD_PROGRESS = "key_download_progress"

        /**
         * 检查新版本
         */
        fun checkNewVersion(context: Context) {
            context.startService(Intent(context, UpgradeService::class.java).apply {
                putExtra(KEY_ACTION, ACTION_CHECK_NEW_VERSION)
            })
        }

        /**
         * 忽略此版本
         */
        fun ignoreThisVersion(context: Context, versionInfo: VersionInfo) {
            context.startService(Intent(context, UpgradeService::class.java).apply {
                putExtra(KEY_ACTION, ACTION_IGNORE_THIS_VERSION)
                putExtra(KEY_VERSION_INFO, versionInfo)
            })
        }

        /**
         * 下载APK
         */
        fun downloadApk(context: Context, versionInfo: VersionInfo) {
            context.startService(Intent(context, UpgradeService::class.java).apply {
                putExtra(KEY_ACTION, ACTION_DOWNLOAD_APK)
                putExtra(KEY_VERSION_INFO, versionInfo)
            })
        }
    }

    private val settingService = Repository.getService<ISettingService>()
    private val commonService = Repository.getService<ICommonService>()

    override fun onCreate() {
        super.onCreate()
        Repository.getProgressBus().downloadProgress.observe(this, Observer {
            if (it.tag == TAG_DOWNLOAD) {
                // 通知调用者
                sendBroadcast(Intent(UpgradeReceiver.ACTION_UPGRADE_RECEIVER).apply {
                    putExtra(KEY_ACTION, ACTION_DOWNLOAD_PROGRESS)
                    putExtra(KEY_DOWNLOAD_PROGRESS, it.percent)
                })
            }
        })
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.getStringExtra(KEY_ACTION)) {
            ACTION_CHECK_NEW_VERSION -> checkNewVersion()
            ACTION_IGNORE_THIS_VERSION -> {
                val versionInfo = intent.getParcelableExtra<VersionInfo>(KEY_VERSION_INFO)
                versionInfo?.let { ignoreThisVersion(it) }
            }
            ACTION_DOWNLOAD_APK -> {
                val versionInfo = intent.getParcelableExtra<VersionInfo>(KEY_VERSION_INFO)
                versionInfo?.let { downloadApk(it) }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    /**
     * 检查新版本
     */
    private fun checkNewVersion() {
        launch {
            try {
                val versionInfo = settingService.checkNewVersion()
                sendBroadcast(Intent(UpgradeReceiver.ACTION_UPGRADE_RECEIVER).apply {
                    putExtra(KEY_ACTION, ACTION_NEW_VERSION)
                    putExtra(KEY_VERSION_INFO, versionInfo)
                })
            } catch (e: Exception) {
                LogUtil.logE(TAG_LOG, Log.getStackTraceString(e))
            }
        }
    }

    /**
     * 忽略此版本
     */
    private fun ignoreThisVersion(versionInfo: VersionInfo) {
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
    private fun downloadApk(versionInfo: VersionInfo) {
        launch {
            try {
                val apkFile = commonService.downloadFile(
                    TAG_DOWNLOAD,
                    versionInfo.url,
                    versionInfo.fileName,
                    versionInfo.md5
                )
                // 通知调用者
                sendBroadcast(Intent(UpgradeReceiver.ACTION_UPGRADE_RECEIVER).apply {
                    putExtra(KEY_ACTION, ACTION_DOWNLOAD_COMPLETED)
                    putExtra(KEY_FILE_APK, apkFile)
                })
            } catch (e: Exception) {
                LogUtil.logE(TAG_LOG, Log.getStackTraceString(e))
            }
        }
    }
}