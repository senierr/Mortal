package com.senierr.mortal.receiver

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
open class UpgradeReceiver(
    private val activity: FragmentActivity
) : BroadcastReceiver() {

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

    /**
     * 新版本回调
     */
    open fun onNewVersion(versionInfo: VersionInfo?) {
        if (versionInfo != null) {
            showNewVersionDialog(activity, versionInfo)
        }
    }

    /**
     * 下载进度回调
     *
     * @param percent 0-100
     */
    open fun onDownloadProgress(percent: Int) {
        NotificationManager.sendUpdateNotification(activity, percent)
    }

    /**
     * 下载完成回调
     */
    open fun onDownloadCompleted(apkFile: File) {
        // 移除下载通知
        NotificationManager.cancel(activity, NotificationManager.NOTIFY_ID_UPDATE)
        AppUtil.installApk(activity, "${activity.packageName}.provider", apkFile)
    }

    /**
     * 显示新版本提示
     */
    private fun showNewVersionDialog(activity: FragmentActivity, versionInfo: VersionInfo) {
        MaterialAlertDialogBuilder(activity)
            .setTitle(R.string.discover_new_version)
            .setMessage(versionInfo.changeLog.replace("\\n", "\n")) // 传输时\n被转义成\\n了
            .setPositiveButton(R.string.upgrade_now) { dialog, _ ->
                UpgradeService.downloadApk(activity, versionInfo)
                dialog.dismiss()
            }
            .setNegativeButton(R.string.upgrade_later) { dialog, _ ->
                dialog.dismiss()
            }
            .setNeutralButton(R.string.ignore_this_version) { dialog, _ ->
                UpgradeService.ignoreThisVersion(activity, versionInfo)
                dialog.dismiss()
            }
            .create()
            .apply {
                show()
                setCanceledOnTouchOutside(false)
                getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(context, R.color.text_hint))
                getButton(DialogInterface.BUTTON_NEUTRAL)
                    .setTextColor(ContextCompat.getColor(context, R.color.text_warn))
            }
    }
}