package com.senierr.mortal.support.ext

import android.view.View
import com.senierr.mortal.support.ui.listener.OnThrottleClickListener

/**
 * View扩展函数
 *
 * @author zhouchunjie
 * @date 2019/6/19 9:46
 */

/**
 * View设置防抖动点击事件
 */
fun View.click(listener: (view: View) -> Unit) {
    this.setOnClickListener(object : OnThrottleClickListener() {
        override fun onThrottleClick(view: View) {
            listener.invoke(view)
        }
    })
}

/**
 * 是否显示，不移除占位
 */
fun View.setVisible(isVisible: Boolean) {
    if (isVisible) {
        this.visibility = View.VISIBLE
    } else {
        this.visibility = View.INVISIBLE
    }
}

/**
 * 是否显示，移除占位
 */
fun View.setGone(isGone: Boolean) {
    if (isGone) {
        this.visibility = View.GONE
    } else {
        this.visibility = View.VISIBLE
    }
}