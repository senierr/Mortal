package com.senierr.base.support.ui

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = createViewBinding(inflater, container)
        return binding?.root
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

    /**
     * 创建视图绑定
     */
    abstract fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB
}