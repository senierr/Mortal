package com.senierr.mortal.ext

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

/**
 * Context扩展函数
 *
 * @author chunjiezhou
 * @date 2021/01/07
 */

/**
 * 显示吐司
 */
fun Context.showToast(@StringRes resId: Int, duration : Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, resId, duration).show()
}

/**
 * 显示吐司
 */
fun Context.showToast(message: String?, duration : Int = Toast.LENGTH_SHORT) {
    message?.let {
        Toast.makeText(this, it, duration).show()
    }
}