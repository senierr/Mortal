package com.senierr.base.support.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle

/**
 * 活动工具类
 *
 * @author zhouchunjie
 * @date 2019/6/17 13:33
 */
object ActivityUtil {

    private val activities = mutableListOf<Activity>()

    private var startActivityCount = 0

    private val lifecycleCallback = object : Application.ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity?) {
        }

        override fun onActivityResumed(activity: Activity?) {
        }

        override fun onActivityStarted(activity: Activity?) {
            startActivityCount++
        }

        override fun onActivityDestroyed(activity: Activity?) {
            activity?.let { remove(it) }
        }

        override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
        }

        override fun onActivityStopped(activity: Activity?) {
            startActivityCount--
        }

        override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {
            activity?.let { add(it) }
        }
    }

    /**
     * 绑定到应用
     */
    fun bindToApplication(application: Application) {
        application.registerActivityLifecycleCallbacks(lifecycleCallback)
    }

    /**
     * 添加进队列
     */
    fun add(activity: Activity) {
        activities.add(activity)
    }

    /**
     * 移除队列
     */
    fun remove(activity: Activity) {
        activities.remove(activity)
    }

    /**
     * 结束所有活动
     */
    fun finishAll() {
        val iterator = activities.iterator()
        while (iterator.hasNext()) {
            val activity = iterator.next()
            iterator.remove()
            activity.finish()
        }
    }

    /**
     * 应用是否在前台
     */
    fun isForeground(): Boolean = startActivityCount > 0
}