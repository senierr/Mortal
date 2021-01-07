package com.senierr.mortal.support.ui

import android.app.Application
import com.senierr.mortal.support.utils.ActivityUtil
import com.senierr.mortal.support.utils.LogUtil

/**
 * 应用基类
 *
 * @author zhouchunjie
 * @date 2019/6/17 13:31
 */
abstract class BaseApplication : Application() {

    abstract fun isDebug(): Boolean

    override fun onCreate() {
        super.onCreate()
        // 初始化工具类
        ActivityUtil.bindToApplication(this)
        LogUtil.isDebug = isDebug()
        LogUtil.tag = javaClass.simpleName
    }
}