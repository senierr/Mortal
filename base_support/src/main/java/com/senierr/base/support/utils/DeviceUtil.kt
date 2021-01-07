package com.senierr.base.support.utils

import android.content.Context
import android.content.res.Configuration
import android.os.Build
import android.text.TextUtils
import java.util.*

/**
 * 设备工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object DeviceUtil {

    /**
     * 获取设备号
     */
    fun getDeviceId(context: Context): String {
        var deviceId = SPUtil.getInstance(context).getString("deviceId")
        if (!TextUtils.isEmpty(deviceId)) {
            return deviceId
        }
        val deviceShort = "35" +
                Build.BOARD.length % 10 + Build.BRAND.length % 10+
                Build.DEVICE.length % 10 + Build.DISPLAY.length % 10 +
                Build.HOST.length % 10 + Build.ID.length % 10 +
                Build.MANUFACTURER.length % 10 + Build.MODEL.length % 10 +
                Build.PRODUCT.length % 10 + Build.TAGS.length % 10 +
                Build.TYPE.length % 10 + Build.USER.length % 10
        val uuid = UUID.randomUUID().toString()
        deviceId = EncryptUtil.encryptMD5ToString(deviceShort + uuid) ?: uuid
        SPUtil.getInstance(context).putString("deviceId", deviceId)
        return deviceId
    }

    /**
     * 判断是否平板设备
     *
     * @param context 上下文
     * @return true:平板,false:手机
     */
    fun isTabletDevice(context: Context): Boolean {
        return context.resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE
    }
}