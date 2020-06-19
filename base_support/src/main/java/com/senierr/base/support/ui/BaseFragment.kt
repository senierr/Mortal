package com.senierr.base.support.ui

import android.content.Context
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment

/**
 * Fragment基类
 *
 * @author zhouchunjie
 * @date 2018/5/28
 */
open class BaseFragment(@LayoutRes contentLayoutId: Int = 0) : Fragment(contentLayoutId) {

    private var lazyCreated = false

    override fun onResume() {
        super.onResume()
        val context = activity
        if (!lazyCreated && context != null) {
            onLazyCreate(context)
            lazyCreated = true
        }
    }

    /**
     * 延迟启动
     *
     * 当页面用户可见可操作（onResume）时，才启动，且仅启动一次
     */
    open fun onLazyCreate(context: Context) {}
}