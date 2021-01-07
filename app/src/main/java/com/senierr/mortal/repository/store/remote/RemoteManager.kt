package com.senierr.mortal.repository.store.remote

import android.content.Context
import com.senierr.mortal.repository.store.remote.interceptor.BmobInterceptor
import com.senierr.mortal.repository.store.remote.interceptor.LoggingInterceptor
import com.senierr.mortal.repository.store.remote.interceptor.ProgressInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络数据管理器
 *
 * @author zhouchunjie
 * @date 2019/11/27
 */
object RemoteManager {

    private const val TIMEOUT = 15 * 1000L

    private const val BASE_URL_GANK = "https://gank.io/api/v2/"
    private const val BASE_URL_BMOB = "https://api2.bmob.cn/"

    // 干货集中营请求器
    private lateinit var retrofitGank: Retrofit
    // Bmob请求器
    private lateinit var retrofitBmob: Retrofit

    fun initialize(context: Context, isDebug: Boolean) {
        retrofitGank = Retrofit.Builder()
            .baseUrl(BASE_URL_GANK)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                    .addInterceptor(ProgressInterceptor())
                    .apply { if (isDebug) addInterceptor(LoggingInterceptor()) }
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build()
            )
            .build()

        retrofitBmob = Retrofit.Builder()
            .baseUrl(BASE_URL_BMOB)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder()
                    .addInterceptor(BmobInterceptor())
                    .apply { if (isDebug) addInterceptor(LoggingInterceptor()) }
                    .connectTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .writeTimeout(TIMEOUT, TimeUnit.MILLISECONDS)
                    .build()
            )
            .build()
    }

    /**
     * 获取干货集中营请求器
     */
    fun getGankHttp(): Retrofit = retrofitGank

    /**
     * 获取Bmob请求器
     */
    fun getBmobHttp(): Retrofit = retrofitBmob
}