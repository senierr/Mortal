package com.senierr.base.support.utils

import android.annotation.SuppressLint
import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * 吐司工具类
 *
 * @author zhouchunjie
 * @date 2017/5/19
 */
@SuppressLint("ShowToast")
object ToastUtil {

    /**
     * 显示短时吐司
     *
     * @param context 上下文
     * @param resId 字符串ID
     */
    fun showShort(context: Context?, @StringRes resId: Int) {
        if (context == null) return
        val toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
        toast?.setText(resId)
        toast?.duration = Toast.LENGTH_SHORT
        toast?.show()
    }

    /**
     * 显示短时吐司
     *
     * @param context 上下文
     * @param message 文本
     */
    fun showShort(context: Context?, message: String?) {
        if (context == null || message == null) return
        val toast = Toast.makeText(context, null, Toast.LENGTH_SHORT)
        toast?.setText(message)
        toast?.duration = Toast.LENGTH_SHORT
        toast?.show()
    }

    /**
     * 显示长时吐司
     *
     * @param context 上下文
     * @param resId 字符串ID
     */
    fun showLong(context: Context?, @StringRes resId: Int) {
        if (context == null) return
        val toast = Toast.makeText(context, null, Toast.LENGTH_LONG)
        toast?.setText(resId)
        toast?.duration = Toast.LENGTH_LONG
        toast?.show()
    }

    /**
     * 显示长时吐司
     *
     * @param context 上下文
     * @param message 文本
     */
    fun showLong(context: Context?, message: String?) {
        if (context == null || message == null) return
        val toast = Toast.makeText(context, null, Toast.LENGTH_LONG)
        toast?.setText(message)
        toast?.duration = Toast.LENGTH_LONG
        toast?.show()
    }
}