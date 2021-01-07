package com.senierr.mortal.support.ext

import androidx.activity.ComponentActivity
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * ViewModel扩展函数
 *
 * @author chunjiezhou
 * @date 2020/12/25
 */

@MainThread
inline fun <reified VM : ViewModel> ComponentActivity.getViewModel(): Lazy<VM> {
    return object : Lazy<VM> {
        private var cached: VM? = null

        override val value: VM
            get() {
                val viewModel = cached
                return viewModel ?: ViewModelProvider(this@getViewModel)
                    .get(VM::class.java).also {
                        cached = it
                    }
            }

        override fun isInitialized() = cached != null
    }
}

@MainThread
inline fun <reified VM : AndroidViewModel> ComponentActivity.getAndroidViewModel(): Lazy<VM> {
    return object : Lazy<VM> {
        private var cached: VM? = null

        override val value: VM
            get() {
                val viewModel = cached
                return viewModel ?: ViewModelProvider(this@getAndroidViewModel,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(application)
                ).get(VM::class.java).also {
                    cached = it
                }
            }

        override fun isInitialized() = cached != null
    }
}

@MainThread
inline fun <reified VM : ViewModel> Fragment.getViewModel(): Lazy<VM> {
    return object : Lazy<VM> {
        private var cached: VM? = null

        override val value: VM
            get() {
                val viewModel = cached
                return viewModel ?: ViewModelProvider(this@getViewModel)
                    .get(VM::class.java).also {
                        cached = it
                    }
            }

        override fun isInitialized() = cached != null
    }
}

@MainThread
inline fun <reified VM : AndroidViewModel> Fragment.getAndroidViewModel(): Lazy<VM> {
    return object : Lazy<VM> {
        private var cached: VM? = null

        override val value: VM
            get() {
                val viewModel = cached
                return viewModel ?: ViewModelProvider(this@getAndroidViewModel,
                    ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
                ).get(VM::class.java).also {
                    cached = it
                }
            }

        override fun isInitialized() = cached != null
    }
}