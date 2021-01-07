package com.senierr.mortal.support.utils

import android.util.Log

/**
 * 日志工具类
 *
 * @author zhouchunjie
 * @date 2017/10/26
 */
object LogUtil {

    var isDebug = false
    var tag: String = LogUtil::class.java.simpleName

    fun logV(msg: String?) {
        logV(tag, msg)
    }

    fun logD(msg: String?) {
        logD(tag, msg)
    }

    fun logI(msg: String?) {
        logI(tag, msg)
    }

    fun logW(msg: String?) {
        logW(tag, msg)
    }

    fun logE(msg: String?) {
        logE(tag, msg)
    }

    fun logV(tag: String, msg: String?) {
        if (isDebug && msg != null) {
            Log.v(tag, msg)
        }
    }

    fun logD(tag: String, msg: String?) {
        if (isDebug && msg != null) {
            Log.d(tag, msg)
        }
    }

    fun logI(tag: String, msg: String?) {
        if (isDebug && msg != null) {
            Log.i(tag, msg)
        }
    }

    fun logW(tag: String, msg: String?) {
        if (isDebug && msg != null) {
            Log.w(tag, msg)
        }
    }

    fun logE(tag: String, msg: String?) {
        if (isDebug && msg != null) {
            Log.e(tag, msg)
        }
    }
}
