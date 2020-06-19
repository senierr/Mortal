package com.senierr.repository.remote.interceptor

import com.senierr.repository.remote.progress.*
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 公共参数拦截器
 *
 * @author zhouchunjie
 * @date 2020/6/11
 */
class ProgressInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val uploadTag = original.header("upload_tag")
        val downloadTag = original.header("download_tag")

        val originalRequestBody = original.body()
        val rawRequest = if (originalRequestBody != null && !uploadTag.isNullOrBlank()) {
            original.newBuilder()
                .method(original.method(),
                    ProgressRequestBody(originalRequestBody, object : OnProgressListener {
                        override fun onProgress(totalSize: Long, currentSize: Long, percent: Int) {
                            ProgressBus.uploadProgress.postValue(Progress(uploadTag, totalSize, currentSize, percent))
                        }
                    })
                ).build()
        } else {
            original.newBuilder().build()
        }
        val originalResponse = chain.proceed(rawRequest)
        val originalResponseBody = originalResponse.body()
        return if (originalResponseBody != null && !downloadTag.isNullOrBlank()) {
            originalResponse.newBuilder()
                .body(ProgressResponseBody(originalResponseBody, object : OnProgressListener {
                    override fun onProgress(totalSize: Long, currentSize: Long, percent: Int) {
                        ProgressBus.downloadProgress.postValue(Progress(downloadTag, totalSize, currentSize, percent))
                    }
                }))
                .build()
        } else {
            originalResponse
        }
    }
}