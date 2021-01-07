package com.senierr.mortal.repository.disk

import android.content.Context
import com.senierr.mortal.support.utils.DiskLruCacheUtil
import com.senierr.mortal.support.utils.StorageUtil
import java.io.File

/**
 * 磁盘管理
 *
 * @author zhouchunjie
 * @date 2019/11/28
 */
object DiskManager {

    private var diskLruCacheUtil: DiskLruCacheUtil? = null

    private lateinit var cacheDir: File
    private lateinit var downloadDir: File

    /**
     * 初始化
     */
    fun initialize(context: Context) {
        val destDir = StorageUtil.getCacheDir(context)
        val destFile = File(destDir, "diskLruCache")
        if (!destFile.exists()) destFile.mkdirs()
        diskLruCacheUtil = DiskLruCacheUtil.getInstance(destFile)

        cacheDir = StorageUtil.getCacheDir(context)
        downloadDir = StorageUtil.getDownloadDir(context)
    }

    /**
     * 获取磁盘缓存
     */
    fun getDiskLruCache(): DiskLruCacheUtil? {
        return diskLruCacheUtil
    }

    /**
     * 获取磁盘缓存目录
     */
    fun getDiskCacheDir(context: Context): File = cacheDir

    /**
     * 获取文件下载目录
     */
    fun getDownloadDir(): File = downloadDir
}