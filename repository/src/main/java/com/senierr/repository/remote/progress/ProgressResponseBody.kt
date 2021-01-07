package com.senierr.repository.remote.progress

import okhttp3.MediaType
import okhttp3.ResponseBody
import okio.*

/**
 * 封装进度回调的响应体
 *
 * @author zhouchunjie
 * @date 2017/9/9
 */
class ProgressResponseBody(
        private val delegate: ResponseBody,
        private val listener: OnProgressListener
) : ResponseBody() {

    companion object {
        // 进度回调最小间隔时长(ms)
        const val REFRESH_MIN_INTERVAL: Long = 100
    }

    private var bufferedSource: BufferedSource? = null

    override fun contentType(): MediaType? {
        return delegate.contentType()
    }

    override fun contentLength(): Long {
        return delegate.contentLength()
    }

    override fun source(): BufferedSource {
        val source = bufferedSource ?: Okio.buffer(CountingSource(delegate.source()))
        bufferedSource = source
        return source
    }

    private inner class CountingSource constructor(delegate: Source) : ForwardingSource(delegate) {

        private var totalBytesRead: Long = 0
        private var contentLength: Long = 0
        private var lastRefreshTime: Long = 0

        override fun read(sink: Buffer, byteCount: Long): Long {
            val bytesRead = super.read(sink, byteCount)
            if (contentLength <= 0) {
                contentLength = contentLength()
            }

            totalBytesRead += if (bytesRead != -1L) bytesRead else 0
            val curTime = System.currentTimeMillis()
            if (curTime - lastRefreshTime >= REFRESH_MIN_INTERVAL || totalBytesRead == contentLength) {
                val percent = if (contentLength <= 0) 100 else (totalBytesRead * 100 / contentLength).toInt()
                listener.onProgress(contentLength, totalBytesRead, percent)
                lastRefreshTime = System.currentTimeMillis()
            }
            return bytesRead
        }
    }
}