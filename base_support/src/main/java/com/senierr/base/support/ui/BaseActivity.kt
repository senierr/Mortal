package com.senierr.base.support.ui

import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity

/**
 * Activity基类
 *
 * @author zhouchunjie
 * @date 2018/5/28
 */
open class BaseActivity(@LayoutRes contentLayoutId: Int = 0) : AppCompatActivity(contentLayoutId)