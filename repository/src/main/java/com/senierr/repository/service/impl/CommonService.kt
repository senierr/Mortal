package com.senierr.repository.service.impl

import com.senierr.base.support.utils.CloseUtil
import com.senierr.repository.disk.DiskManager
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.CommonApi
import com.senierr.repository.remote.progress.OnProgressListener
import com.senierr.repository.remote.progress.ProgressResponseBody
import com.senierr.repository.service.api.ICommonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio
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

    override suspend fun downloadFile(tag: String, url: String, destName: String): File {
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
                        val result = destFile.delete()
                        if (!result) {
                            throw Exception(destFile.path + " delete failed!")
                        }
                    }

                    var bufferedSource: BufferedSource? = null
                    var bufferedSink: BufferedSink? = null
                    try {
                        bufferedSource = Okio.buffer(Okio.source(responseBody.byteStream()))
                        bufferedSink = Okio.buffer(Okio.sink(destFile))

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