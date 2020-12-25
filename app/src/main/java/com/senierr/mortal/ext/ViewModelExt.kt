package com.senierr.mortal.ext

import android.app.Application
import androidx.annotation.MainThread
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner

/**
 * ViewModel扩展函数
 *
 * @author chunjiezhou
 * @date 2020/12/25
 */

@MainThread
inline fun <reified VM : ViewModel> ViewModelStoreOwner.getViewModel(): VM {
    return ViewModelProvider(this).get(VM::class.java)
}

@MainThread
inline fun <reified VM : AndroidViewModel> ViewModelStoreOwner.getAndroidViewModel(application: Application): VM {
    return ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(application)).get(VM::class.java)
}