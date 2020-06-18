package com.senierr.base.support.ui.listener

import android.view.View

/**
 * 过滤重复点击事件
 *
 * @author zhouchunjie
 * @date 2018/4/16
 */
abstract class OnThrottleClickListener : View.OnClickListener {

    private var lastClickTime = 0L
    private var viewId = 0

    final override fun onClick(v: View?) {
        if (v == null) return
        val currentTime = System.currentTimeMillis()
        val isSameView = v.id == viewId
        val isFastClick = currentTime - lastClickTime < 300
        viewId = v.id
        lastClickTime = currentTime
        if (isFastClick && isSameView) return
        onThrottleClick(v)
    }

    abstract fun onThrottleClick(view: View)
}