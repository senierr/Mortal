package com.senierr.mortal.support.utils

import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat

/**
 * Drawable工具类
 *
 * @author zhouchunjie
 * @date 2018/9/11
 */
object DrawableUtil {

    /**
     * 获取可以进行tint的Drawable
     */
    fun getStateDrawable(drawable: Drawable): Drawable {
        val state = drawable.constantState
        return DrawableCompat.wrap(if (state == null) drawable else state.newDrawable()).mutate()
    }

    /**
     * 获取选择器Drawable
     *
     * xml中写的"selector"标签映射对象就是StateListDrawable对象
     *
     * @param states
     * Key: state, 例如: {android.R.attr.state_enabled, -android.R.attr.state_pressed}
     * Value: Drawable
     */
    fun getStateListDrawable(states: MutableMap<IntArray, Drawable>): StateListDrawable {
        val stateListDrawable = StateListDrawable()
        for (state in states) {
            stateListDrawable.addState(state.key, state.value)
        }
        return stateListDrawable
    }

    /**
     * 对Drawable着色
     */
    fun tintDrawable(drawable: Drawable, color: Int): Drawable {
        val wrappedDrawable = getStateDrawable(drawable)
        DrawableCompat.setTint(wrappedDrawable, color)
        return wrappedDrawable
    }

    /**
     * 对Drawable着色
     */
    fun tintDrawable(context: Context, @DrawableRes drawableRes: Int, @ColorRes colorRes: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(context, drawableRes)
        val color = ContextCompat.getColor(context, colorRes)
        if (drawable != null) {
            return tintDrawable(drawable, color)
        }
        return null
    }
}