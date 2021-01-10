package com.senierr.repository.service.impl

import com.senierr.base.support.utils.CloseUtil
import com.senierr.repository.disk.DiskManager
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.CommonApi
import com.senierr.repository.remote.progress.*
import com.senierr.repository.service.api.ICommonService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.isActive
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okio.BufferedSink
import okio.BufferedSource
import okio.Okio
import java.io.File
import java.io.IOException
import java.net.URLConnection
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException


/**
 *
 * @author zhouchunjie
 * @date 2020/5/25
 */
class CommonService : ICommonService {

    private val commonApi by lazy { RemoteManager.getGankHttp().create(CommonApi::class.java) }

    override suspend fun uploadFile(file: File, onUploadListener: OnProgressListener): Boolean {
        return withContext(Dispatchers.IO) {
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("param1", "param1")
                .addFormDataPart("key", file.name, RequestBody.create(guessMimeType(file.path), file))
                .build()
            val response = commonApi.uploadFile(ProgressRequestBody(requestBody, onUploadListener))
            return@withContext response.isSuccessful()
        }
    }

    override suspend fun downloadFile(
        url: String, destName:
        String, md5: String,
        onProgressListener: OnProgressListener?
    ): File {
        return withContext(Dispatchers.IO) {
            suspendCancellableCoroutine { continuation ->
                try {
                    val call = commonApi.downloadFile(url)
                    continuation.invokeOnCancellation {
                        call.cancel()
                    }

                    val rawResponseBody = call.execute().body()?: throw IOException("ResponseBody is null.")
                    val realResponseBody = if (onProgressListener != null) {
                        ProgressResponseBody(rawResponseBody, onProgressListener)
                    } else {
                        rawResponseBody
                    }

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
                        bufferedSource = Okio.buffer(Okio.source(realResponseBody.byteStream()))
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

    /**
     * 获取可能的内容格式
     */
    private fun guessMimeType(
        path: String,
        defaultContentType: String = "application/octet-stream"
    ): MediaType? {
        val result = path.replace("#", "")   //解决文件名中含有#号异常的问题
        val fileNameMap = URLConnection.getFileNameMap()
        var contentType = fileNameMap.getContentTypeFor(result)
        if (contentType == null) {
            contentType = defaultContentType
        }
        return MediaType.parse(contentType)
    }
}