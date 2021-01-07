package com.senierr.mortal.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

/**
 * 带生命周期的服务
 *
 * @author chunjiezhou
 * @date 2020/12/29
 */
open class LifecycleService : Service(), LifecycleOwner, CoroutineScope by MainScope() {

    private lateinit var lifecycleRegistry: LifecycleRegistry

    override fun onCreate() {
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE)
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START)
        return null
    }

    override fun onUnbind(intent: Intent?): Boolean {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP)
        return super.onUnbind(intent)
    }

    override fun onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        cancel()
        super.onDestroy()
    }

    override fun getLifecycle(): Lifecycle = lifecycleRegistry
}