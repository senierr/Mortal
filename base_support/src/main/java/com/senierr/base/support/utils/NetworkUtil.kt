package com.senierr.base.support.utils

import android.Manifest
import android.content.Context
import android.net.ConnectivityManager
import androidx.annotation.RequiresPermission

/**
 * 网络工具类
 *
 * 7.0以上网络监听广播需要动态注册
 *
 * @author zhouchunjie
 * @date 2017/9/14
 */
object NetworkUtil {

    const val NETWORK_NONE = -1
    const val NETWORK_MOBILE = 0
    const val NETWORK_WIFI = 1

    /**
     * 获取网络类型
     */
    @Suppress("DEPRECATION")
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun getNetworkType(context: Context): Int {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = connectivityManager?.activeNetworkInfo
        if (networkInfo != null && networkInfo.isConnected) {
            return when (networkInfo.type) {
                ConnectivityManager.TYPE_MOBILE -> NETWORK_MOBILE
                ConnectivityManager.TYPE_WIFI -> NETWORK_WIFI
                else -> NETWORK_NONE
            }
        } else {
            return NETWORK_NONE
        }
    }

    /**
     * 判断网络是否连接
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    fun isConnected(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager?
        val networkInfo = connectivityManager?.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
