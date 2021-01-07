package com.senierr.base.support.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.core.content.FileProvider
import android.text.TextUtils
import java.io.File

/**
 * 意图工具类
 *
 * @author zhouchunjie
 * @date 2019/6/24
 */
object IntentUtil {

    /**
     * 获取跳转到指定应用市场的意图
     *
     * @param packageName 需要跳转的应用包名
     * @param storePackageName 应用市场包名
     */
    fun getOpenMarketIntent(packageName: String, storePackageName: String): Intent {
        val uri = Uri.parse("market://details?id=$packageName")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (!TextUtils.isEmpty(storePackageName)) {
            intent.setPackage(storePackageName)
        }
        return intent
    }

    /**
     * 获取打开设置的意图
     *
     * @param packageName 包名
     */
    fun getOpenAppDetailsSettingsIntent(packageName: String): Intent {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        intent.data = Uri.parse("package:$packageName")
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return intent
    }

    /**
     * 获取打开App的意图
     *
     * @param context 上下文
     * @param packageName 包名
     */
    fun getLaunchAppIntent(context: Context, packageName: String): Intent? {
        return context.packageManager.getLaunchIntentForPackage(packageName)
    }

    /**
     * 获取打开普通视图的意图
     */
    fun getNormalViewIntent(uri: Uri): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri
        return intent
    }

    /**
     * 获取打开文件的意图
     */
    fun getOpenFileIntent(context: Context, authority: String, file: File): Intent {
        val extend = file.extension
        var type = ""
        if (extend == "doc" || extend == "docx") {
            type = "application/msword"
        } else if (extend == "xls" || extend == "xlsx") {
            type = "application/vnd.ms-excel"
        } else if (extend == "ppt" || extend == "pptx") {
            type = "application/vnd.ms-powerpoint"
        } else if (extend == "txt" || extend == "log" || extend == "xml") {
            type = "text/plain"
        } else if (extend == "pdf") {
            type = "application/pdf"
        } else if (extend == "rtf") {
            type = "application/rtf"
        } else if (extend == "m4a" || extend == "mp3" || extend == "mid" || extend == "xmf" || extend == "ogg" || extend == "wav") {
            type = "audio/*"
        } else if (extend == "3gp" || extend == "mp4" || extend == "avi") {
            type = "video/*"
        } else if (extend == "jpg" || extend == "gif" || extend == "png" || extend == "jpeg" || extend == "bmp" || extend == "ico") {
            type = "image/*"
        } else if (extend == "apk") {
            type = "application/vnd.android.package-archive"
        } else if (extend == "chm") {
            type = "application/x-chm"
        } else if (extend == "htm" || extend == "html") {
            type = "text/html"
        } else if (extend == "zip") {
            type = "application/zip"
        } else if (extend == "rar") {
            type = "application/x-rar-compressed"
        }

        val intent = Intent()
        intent.action = Intent.ACTION_VIEW
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(context, authority, file)
        } else {
            Uri.fromFile(file)
        }
        intent.setDataAndType(uri, type)
        return intent
    }

    /**
     * 获取打开邮箱的意图
     */
    fun getOpenEmailIntent(email: String): Intent {
        return Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:$email"))
    }
}