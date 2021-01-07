package com.senierr.base.support.utils

import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.math.BigDecimal

/**
 * 文件工具类
 *
 * @author zhouchunjie
 * @date 2017/4/21
 */
object FileUtil {

    /**
     * 获取文件的byte[]
     */
    fun getBytes(file: File?): ByteArray? {
        if (file == null || !file.exists()) return null
        var fis: FileInputStream? = null
        var bos: ByteArrayOutputStream? = null
        try {
            fis = FileInputStream(file)
            bos = ByteArrayOutputStream()
            val b = ByteArray(1024)
            var n: Int
            while (fis.read(b).also { n = it } != -1) {
                bos.write(b, 0, n)
            }
            return bos.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            CloseUtil.closeIOQuietly(fis, bos)
        }
        return null
    }

    /**
     * 获取文件/文件夹的大小
     */
    fun getFileSize(file: File?): Long {
        var size: Long = 0
        if (file == null || !file.exists()) return size
        if (file.isDirectory) {
            val fileList = file.listFiles()
            if (fileList != null && fileList.isNotEmpty()) {
                for (temp in fileList) {
                    size += getFileSize(temp)
                }
            }
        } else {
            size += file.length()
        }
        return size
    }

    /**
     * 删除文件/文件夹及内容
     */
    fun deleteFile(file: File?): Boolean {
        if (file == null || !file.exists()) return true
        if (file.isDirectory) {
            val fileList = file.listFiles()
            if (fileList != null && fileList.isNotEmpty()) {
                for (temp in fileList) {
                    val success = deleteFile(temp)
                    if (!success) return false
                }
            }
        }
        return file.delete()
    }

    /**
     * 格式化单位
     */
    fun getFormatSize(size: Double): String {
        val kiloByte = size / 1024
        if (kiloByte < 1) {
            return "0B"
        }

        val megaByte = kiloByte / 1024
        if (megaByte < 1) {
            val result1 = BigDecimal(kiloByte.toString())
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "KB"
        }

        val gigaByte = megaByte / 1024
        if (gigaByte < 1) {
            val result2 = BigDecimal(megaByte.toString())
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "MB"
        }

        val teraBytes = gigaByte / 1024
        if (teraBytes < 1) {
            val result3 = BigDecimal(gigaByte.toString())
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                .toPlainString() + "GB"
        }
        val result4 = BigDecimal(teraBytes)
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + "TB"
    }
}
