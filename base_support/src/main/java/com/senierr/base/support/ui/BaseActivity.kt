package com.senierr.base.support.ui

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * Activity基类
 *
 * @author zhouchunjie
 * @date 2018/5/28
 */
@ExperimentalCoroutinesApi
open class BaseActivity(
    @LayoutRes contentLayoutId: Int = 0
) : AppCompatActivity(contentLayoutId), CoroutineScope by MainScope() {

    override fun onDestroy() {
        cancel()
        super.onDestroy()
    }
}