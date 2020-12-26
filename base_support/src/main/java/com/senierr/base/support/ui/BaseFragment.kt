package com.senierr.base.support.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding

/**
 * Fragment基类
 *
 * @author zhouchunjie
 * @date 2018/5/28
 */
abstract class BaseFragment<VB: ViewBinding> : Fragment() {

    protected var binding: VB? = null
    private var lazyCreated = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = createViewBinding(inflater, container)
        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    override fun onResume() {
        super.onResume()
        val context = activity
        if (!lazyCreated && context != null) {
            onLazyCreate(context)
            lazyCreated = true
        }
    }

    /**
     * 创建视图绑定
     */
    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB

    /**
     * 延迟启动
     *
     * 当页面用户可见可操作（onResume）时，才启动，且仅启动一次
     */
    open fun onLazyCreate(context: Context) {
    }
}