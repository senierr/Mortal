package com.senierr.base.support.utils

import com.jakewharton.disklrucache.DiskLruCache
import java.io.*
import java.nio.charset.Charset

/**
 * 磁盘缓存工具类
 *
 * @author zhouchunjie
 * @date 2019/6/26
 */
class DiskLruCacheUtil private constructor(private val diskLruCache: DiskLruCache) {

    companion object {
        // 最大缓存字节
        private const val MAX_SIZE = 10L * 1024 * 1024
        // 默认应用版本号
        private const val APP_VERSION = 1
        // 默认KEY对应数据数量
        private const val VALUE_COUNT = 1

        fun getInstance(directory: File, appVersion: Int = APP_VERSION, maxSize: Long = MAX_SIZE): DiskLruCacheUtil? {
            try {
                val diskLruCache = DiskLruCache.open(directory, appVersion, VALUE_COUNT, maxSize)
                return DiskLruCacheUtil(diskLruCache)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }
    }

    /**
     * 存String
     */
    fun putString(key: String, value: String) {
        val editor = getEditor(key) ?: return
        var bw: BufferedWriter? = null
        try {
            bw = BufferedWriter(OutputStreamWriter(editor.newOutputStream(0)))
            bw.write(value)
            editor.commit()
        } catch (e: IOException) {
            e.printStackTrace()
            editor.abortSafe()
        } finally {
            CloseUtil.closeIOQuietly(bw)
        }
    }

    /**
     * 取String
     */
    fun getString(key: String): String? {
        val inputStream = getInputStream(key) ?: return null
        try {
            val sb = StringBuilder()
            val buffer = ByteArray(256)
            var len: Int
            while (inputStream.read(buffer).also { len = it } != -1) {
                sb.append(String(buffer, 0, len))
            }
            return sb.toString()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            CloseUtil.closeIOQuietly(inputStream)
        }
        return null
    }

    /**
     * 存ByteArray
     */
    fun putByteArray(key: String, value: ByteArray) {
        val editor = getEditor(key) ?: return
        var os: OutputStream? = null
        try {
            os = editor.newOutputStream(0)
            os.write(value)
            os.flush()
            editor.commit()
        } catch (e: IOException) {
            e.printStackTrace()
            editor.abortSafe()
        } finally {
            CloseUtil.closeIOQuietly(os)
        }
    }

    /**
     * 取ByteArray
     */
    fun getByteArray(key: String): ByteArray? {
        val inputStream = getInputStream(key) ?: return null
        try {
            val bos = ByteArrayOutputStream()
            val buf = ByteArray(256)
            var len: Int
            while (inputStream.read(buf).also { len = it } != -1) {
                bos.write(buf, 0, len)
            }
            return bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            CloseUtil.closeIOQuietly(inputStream)
        }
        return null
    }

    /**
     * 存Serializable
     */
    fun putSerializable(key: String, value: Serializable) {
        val editor = getEditor(key) ?: return
        var oos: ObjectOutputStream? = null
        try {
            oos = ObjectOutputStream(editor.newOutputStream(0))
            oos.writeObject(value)
            oos.flush()
            editor.commit()
        } catch (e: IOException) {
            e.printStackTrace()
            editor.abortSafe()
        } finally {
            CloseUtil.closeIOQuietly(oos)
        }
    }

    /**
     * 取Serializable
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getSerializable(key: String): T? {
        val inputStream = getInputStream(key) ?: return null

        var ois: ObjectInputStream? = null
        try {
            ois = ObjectInputStream(inputStream)
            return ois.readObject() as T?
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            CloseUtil.closeIOQuietly(ois)
        }
        return null
    }

    /**
     * 移除一条记录
     */
    fun remove(key: String): Boolean {
        try {
            return diskLruCache.remove(formatKey(key))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return false
    }

    fun close() {
        try {
            diskLruCache.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun delete() {
        try {
            diskLruCache.delete()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun flush() {
        try {
            diskLruCache.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun isClosed(): Boolean {
        return diskLruCache.isClosed
    }

    fun size(): Long {
        return diskLruCache.size()
    }

    /**
     * 格式化Key
     */
    private fun formatKey(key: String): String? {
        return EncryptUtil.encryptMD5ToString(key)?.toLowerCase()
    }

    /**
     * 获取编辑器
     */
    private fun getEditor(key: String): DiskLruCache.Editor? {
        var edit: DiskLruCache.Editor? = null
        try {
            edit = diskLruCache.edit(formatKey(key))
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return edit
    }

    /**
     * 获取输入流
     */
    private fun getInputStream(key: String): InputStream? {
        try {
            val snapshot = diskLruCache.get(formatKey(key))
            return snapshot?.getInputStream(0)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    /**
     * 安全的终止编辑
     */
    private fun DiskLruCache.Editor.abortSafe() {
        try {
            this.abort()
        } catch (e1: IOException) {
        }
    }
}