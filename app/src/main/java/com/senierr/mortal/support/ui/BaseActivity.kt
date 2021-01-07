package com.senierr.mortal.support.ui

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

/**
 * Activity基类
 *
 * @author zhouchunjie
 * @date 2018/5/28
 */
abstract class BaseActivity<VB: ViewBinding> : AppCompatActivity() {

    protected lateinit var binding: VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = createViewBinding(layoutInflater)
        setContentView(binding.root)
    }

    /**
     * 创建视图绑定
     */
    abstract fun createViewBinding(layoutInflater: LayoutInflater): VB
}