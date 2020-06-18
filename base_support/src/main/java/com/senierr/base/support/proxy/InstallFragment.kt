package com.senierr.base.support.proxy

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import com.senierr.base.support.ui.BaseFragment
import java.io.File

/**
 * 代理Fragment
 *
 * 主要用于请求安装未知来源应用权限
 *
 * @author zhouchunjie
 * @date 2019/6/14 11:04
 */
class InstallFragment : BaseFragment() {

    companion object {
        const val TAG = "InstallFragment"
        const val CODE_MANAGE_UNKNOWN_APP_SOURCES = 100
    }

    var authority: String? = null
    var apkFile: File? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == CODE_MANAGE_UNKNOWN_APP_SOURCES) {
            context?.let {
                installApkAbove24(it, authority, apkFile)
            }
        }
    }

    /**
     * 已兼容至8.0(26)
     */
    fun installApk() {
        context?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // 8.0(26)以上安装APK
                val hasInstallPermission = it.packageManager.canRequestPackageInstalls()
                if (!hasInstallPermission) {
                    // 没有安装未知应用权限
                    val uri = Uri.parse("package:" + it.packageName)
                    val intent = Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, uri)
                    startActivityForResult(intent, CODE_MANAGE_UNKNOWN_APP_SOURCES)
                } else { // 拥有权限
                    installApkAbove24(it, authority, apkFile)
                }
            } else {
                installApkAbove24(it, authority, apkFile)
            }
        }
    }

    /**
     * 安装APK
     *
     * 7.0适配
     */
    private fun installApkAbove24(context: Context?, authority: String?, apkFile: File?) {
        if (context == null || authority == null || apkFile == null) return
        val intent = Intent(Intent.ACTION_VIEW)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
            val contentUri = FileProvider.getUriForFile(context, authority, apkFile)
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive")
        } else {
            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive")
        }
        context.startActivity(intent)
    }
}