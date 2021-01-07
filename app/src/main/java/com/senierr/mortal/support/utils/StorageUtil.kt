package com.senierr.mortal.support.utils

import android.content.Context
import android.os.Environment
import java.io.File

/**
 * 存储工具类
 *
 * 1. Context#getCacheDir()                    /data/data/cn.appname.xxx/cache
 * 2. Context#getDir("spanner",MODE_PRIVATE)   /data/data/cn.appname.xxx/app_spanner
 * 3. Context#getFileDir()                     /data/data/cn.appname.xxx/files
 *
 * 3. Context#getExternalCacheDir()            /storage/emulated/0/Android/data/cn.appname.xxx/cache
 * 4. Context#getExternalFilesDir(Environment.DIRECTORY_PICTURES)  /storage/emulated/0/Android/data/cn.appname.xxx/files/Pictures
 *    Context#getExternalFilesDir(null)        /storage/emulated/0/Android/data/cn.appname.xxx/files
 * 5. Context#getExternalMediaDirs()           /storage/emulated/0/Android/media/cn.appname.xxx
 *
 *
 * 1. Environment#getExternalStorageDirectory()                /storage/emulated/0
 * 2. Environment#getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)        /storage/emulated/0/DCIM
 *    Environment#getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)    /storage/emulated/0/Pictures
 * 3. Environment#getDataDirectory()                           /data
 * 4. Environment#getDownloadCacheDirectory()                  /data/cache
 * 5. Environment#getRootDirectory()                           /system
 *
 * @author zhouchunjie
 * @date 2019/7/18
 */
object StorageUtil {

    /**
     * SD卡（外部存储）是否可用
     */
    fun isSDCardEnable(): Boolean = Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()

    /**
     * 获取缓存目录
     *
     * 1. /storage/emulated/0/Android/data/cn.appname.xxx/cache
     * 2. /data/data/cn.appname.xxx/cache
     */
    fun getCacheDir(context: Context, isExternal: Boolean = true): File {
        return if (isSDCardEnable() && isExternal) {
            context.externalCacheDir ?: context.cacheDir
        } else {
            context.cacheDir
        }
    }

    /**
     * 获取下载目录
     *
     * 1. /storage/emulated/0/Android/data/cn.appname.xxx/files/download
     * 2. /data/data/cn.appname.xxx/files/download
     */
    fun getDownloadDir(context: Context, isExternal: Boolean = true): File {
        return getFileDir(context, "download", isExternal)
    }

    /**
     * 获取自定义文件夹
     *
     * 1. /storage/emulated/0/Android/data/cn.appname.xxx/files/type
     * 2. /data/data/cn.appname.xxx/files/type
     */
    fun getFileDir(context: Context, type: String, isExternal: Boolean = true): File {
        if (isSDCardEnable() && isExternal) {
            val dir = context.getExternalFilesDir(type)
            if (dir != null) return dir
        }
        val dir = File(context.filesDir, type)
        if (!dir.exists()) dir.mkdirs()
        return dir
    }
}