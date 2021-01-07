package com.senierr.mortal.support.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager

/**
 * 屏幕工具类
 *
 * @author zhouchunjie
 * @date 2017/10/30
 */
object ScreenUtil {

    /**
     * 获取状态栏高度
     *
     * @return 状态栏高度px
     */
    fun getStatusBarHeight(context: Context): Int {
        val resources = context.resources
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

    /**
     * 获取屏幕的宽度（单位：px）
     *
     * @return 屏幕宽px
     */
    fun getScreenWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val dm = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(dm)
        return dm.widthPixels
    }

    /**
     * 获取屏幕的高度（单位：px）
     *
     * @return 屏幕高px
     */
    fun getScreenHeight(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?
        val dm = DisplayMetrics()
        windowManager?.defaultDisplay?.getMetrics(dm)
        return dm.heightPixels
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    fun px2dp(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    fun px2sp(context: Context, pxValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (pxValue / fontScale + 0.5f).toInt()
    }
}