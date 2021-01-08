package com.senierr.repository.remote.progress

import android.content.BroadcastReceiver
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.senierr.base.support.utils.AppUtil
import com.senierr.mortal.R
import com.senierr.mortal.notification.NotificationManager
import com.senierr.mortal.service.UpgradeService
import com.senierr.repository.entity.bmob.VersionInfo
import java.io.File

/**
 * 升级更新广播接收器
 *
 * @author zhouchunjie
 * @date 2021/1/7 22:55
 */
open class ProgressReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_UPGRADE_RECEIVER = "action_upgrade_receiver"
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null && intent.action == ACTION_UPGRADE_RECEIVER) {
            when (intent.getStringExtra(UpgradeService.KEY_ACTION)) {
                UpgradeService.ACTION_NEW_VERSION -> {
                    val versionInfo = intent.getParcelableExtra<VersionInfo>(UpgradeService.KEY_VERSION_INFO)
                    onNewVersion(versionInfo)
                }
                UpgradeService.ACTION_DOWNLOAD_PROGRESS -> {
                    val percent = intent.getIntExtra(UpgradeService.KEY_DOWNLOAD_PROGRESS, 0)
                    onDownloadProgress(percent)
                }
                UpgradeService.ACTION_DOWNLOAD_COMPLETED -> {
                    val apkFile = intent.getSerializableExtra(UpgradeService.KEY_FILE_APK) as File?
                    apkFile?.let { onDownloadCompleted(it) }
                }
                else -> {}
            }
        }
    }
}