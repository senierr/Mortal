package com.senierr.mortal.ext

import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import java.io.File

/**
 * 图片控件扩展函数
 *
 * @author zhouchunjie
 * @date 2019/8/5
 */

/**
 * 加载网络图片
 */
fun ImageView.show(url: String?,
                   @DrawableRes placeholderRes: Int? = null,
                   @DrawableRes errorRes: Int? = null,
                   isGif: Boolean = false,
                   cookies: MutableMap<String, String>? = null
) {
    if (url.isNullOrBlank()) {
        // TODO 加载占位图
        return
    }
    // 处理Gif
    val requestBuilder = if (isGif) {
        Glide.with(this).asGif()
    } else {
        Glide.with(this).asBitmap()
    }
    // 处理Cookie
    val glideUrl = if (cookies.isNullOrEmpty()) {
        GlideUrl(url)
    } else {
        val builder = LazyHeaders.Builder()
        cookies.forEach {
            builder.addHeader(it.key, it.value)
        }
        GlideUrl(url, builder.build())
    }

    requestBuilder.load(glideUrl)
        .apply {
            // 占位图
            if (placeholderRes != null) placeholder(placeholderRes)
        }
        .apply {
            // 加载失败图
            if (errorRes != null) error(errorRes)
        }
        .into(this)
}

/**
 * 加载本地文件图片
 */
fun ImageView.show(file: File?,
                   @DrawableRes placeholderRes: Int? = null,
                   @DrawableRes errorRes: Int? = null,
                   isGif: Boolean = false
) {
    // 处理Gif
    val requestBuilder = if (isGif) {
        Glide.with(this).asGif()
    } else {
        Glide.with(this).asBitmap()
    }

    requestBuilder.load(file)
        .apply {
            // 占位图
            if (placeholderRes != null) placeholder(placeholderRes)
        }
        .apply {
            // 加载失败图
            if (errorRes != null) error(errorRes)
        }
        .into(this)
}

/**
 * 加载资源文件图片
 */
fun ImageView.show(@DrawableRes resId: Int?,
                   @DrawableRes placeholderRes: Int? = null,
                   @DrawableRes errorRes: Int? = null,
                   isGif: Boolean = false
) {
    // 处理Gif
    val requestBuilder = if (isGif) {
        Glide.with(this).asGif()
    } else {
        Glide.with(this).asBitmap()
    }

    requestBuilder.load(resId)
        .apply {
            // 占位图
            if (placeholderRes != null) placeholder(placeholderRes)
        }
        .apply {
            // 加载失败图
            if (errorRes != null) error(errorRes)
        }
        .into(this)
}