package com.senierr.mortal.repository.store.remote.interceptor

import com.google.gson.Gson
import com.senierr.mortal.support.utils.EncryptUtil
import com.senierr.mortal.repository.entity.bmob.BmobException
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.security.SecureRandom
import java.util.*

/**
 * 公共参数拦截器
 *
 * @author zhouchunjie
 * @date 2020/6/11
 */
class BmobInterceptor : Interceptor {

    companion object {
        private const val APPLICATION_ID = "834b0ae723e4d0a8554694939e659165"
        private const val REST_API_KEY = "038ba9996a660075ecffbea77759178c"
        private const val SECRET_KEY = "19705cefcd82363e"
        private const val SAFE_TOKEN = "ASDFGH"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        // 注入参数
        val url = original.url().newBuilder().build()
        val timeStamp = System.currentTimeMillis().toString()
        val noncestrKey = createNoncestrKey()
        val rawRequest = original.newBuilder()
//            .addHeader("Content-Type", "application/json")
//            .addHeader("X-Bmob-Application-Id", APPLICATION_ID)
//            .addHeader("X-Bmob-REST-API-Key", REST_API_KEY)
            .addHeader("X-Bmob-SDK-Type", "API")
            .addHeader("X-Bmob-Safe-Timestamp", timeStamp)
            .addHeader("X-Bmob-Noncestr-Key", noncestrKey)
            .addHeader("X-Bmob-Secret-Key", SECRET_KEY)
            .addHeader("X-Bmob-Safe-Sign", createSafeSign(url.encodedPath(), timeStamp, SAFE_TOKEN, noncestrKey))
            .method(original.method(), original.body())
            .url(url)
            .build()

        // 解析错误
        val originalResponse = chain.proceed(rawRequest)
        if (originalResponse.code() >= 400) {
            val originalResponseBody = originalResponse.body()
            if (originalResponseBody != null) {
                val bmobException = try {
                    Gson().fromJson(originalResponseBody.string(), BmobException::class.java)
                } catch (e: Exception) {
                    // intercept中只能抛出IOException
                    throw IOException(e.cause)
                }
                throw bmobException
            }
        }
        return originalResponse
    }

    /**
     * 生成随机码，长度16个字符
     */
    private fun createNoncestrKey(): String {
        val sb = StringBuilder()
        val rd = SecureRandom()
        for (i in 0..15) {
            when (rd.nextInt(3)) {
                0 -> sb.append(rd.nextInt(10))  // 0-9的随机数
                1 -> sb.append((rd.nextInt(25) + 65).toChar())  // ASCII在65-90之间为大写,获取大写随机
                2 -> sb.append((rd.nextInt(25) + 97).toChar())  // ASCII在97-122之间为小写，获取小写随机
            }
        }
        return sb.toString()
    }

    /**
     * 生成签名
     */
    private fun createSafeSign(url: String, timeStamp: String, safeToken: String, noncestrKey: String): String {
        val safeSign = EncryptUtil.encryptMD5ToString(url + timeStamp + safeToken + noncestrKey) ?: ""
        return safeSign.toLowerCase(Locale.ROOT)
    }
}