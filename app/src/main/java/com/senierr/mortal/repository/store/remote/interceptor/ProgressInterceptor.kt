package com.senierr.mortal.repository.store.remote.interceptor

import com.senierr.mortal.repository.store.remote.progress.*
import okhttp3.Interceptor
import okhttp3.Response

/**
 * 进度拦截器
 *
 * @author zhouchunjie
 * @date 2020/6/11
 */
class ProgressInterceptor : Interceptor {

    companion object {
        const val HEADER_TAG_UPLOAD = "header_tag_upload"
        const val HEADER_TAG_DOWNLOAD = "header_tag_download"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        val uploadTag = original.header(HEADER_TAG_UPLOAD)
        val downloadTag = original.header(HEADER_TAG_DOWNLOAD)

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