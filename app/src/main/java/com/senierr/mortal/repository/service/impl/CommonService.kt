package com.senierr.mortal.repository.service.impl

import com.senierr.mortal.support.utils.CloseUtil
import com.senierr.mortal.support.utils.EncryptUtil
import com.senierr.mortal.repository.disk.DiskManager
import com.senierr.mortal.repository.remote.RemoteManager
import com.senierr.mortal.repository.remote.api.CommonApi
import com.senierr.mortal.repository.service.api.ICommonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okio.*
import java.io.File
import java.io.IOException
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class CommonService : ICommonService {

    private val commonApi by lazy {
        RemoteManager.getGankHttp().create(CommonApi::class.java)
    }

        override suspend fun downloadFile(tag: String, url: String, destName: String, md5: String): File {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine<File> { continuation ->
                try {
                    val call = commonApi.downloadFile(url, tag)
                    continuation.invokeOnCancellation {
                        call.cancel()
                    }
                    val responseBody = call.execute().body()?: throw IOException("ResponseBody is null.")

                    val destDir = DiskManager.getDownloadDir()

                    // 判断路径是否存在
                    if (!destDir.exists()) {
                        val result = destDir.mkdirs()
                        if (!result) {
                            throw Exception(destDir.path + " create failed!")
                        }
                    }

                    val destFile = File(destDir, destName)
                    // 判断文件是否存在
                    if (destFile.exists()) {
                        // 判断是否需要重新下载
                        val destMD5 = EncryptUtil.encryptMD5File2String(destFile)
                        if (destMD5 == md5) {
                            continuation.resume(destFile)
                            return@suspendCancellableCoroutine
                        }
                        val result = destFile.delete()
                        if (!result) {
                            throw Exception(destFile.path + " delete failed!")
                        }
                    }

                    var bufferedSource: BufferedSource? = null
                    var bufferedSink: BufferedSink? = null
                    try {
                        bufferedSource = responseBody.byteStream().source().buffer()
                        bufferedSink = destFile.sink().buffer()

                        val bytes = ByteArray(1024)
                        var len = 0
                        while (isActive && bufferedSource.read(bytes).also { len = it } != -1) {
                            bufferedSink.write(bytes, 0, len)
                        }
                        bufferedSink.flush()
                        continuation.resume(destFile)
                    } finally {
                        CloseUtil.closeIOQuietly(bufferedSource, bufferedSink)
                    }
                } catch (e: Exception) {
                    continuation.resumeWithException(e)
                }
            }
        }
    }
}