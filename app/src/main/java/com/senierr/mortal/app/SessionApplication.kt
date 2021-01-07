package com.senierr.mortal.app

import androidx.multidex.MultiDexApplication
import com.senierr.mortal.repository.Repository
import com.senierr.mortal.support.utils.ActivityUtil
import com.senierr.mortal.support.utils.LogUtil

/**
 *
 * @author zhouchunjie
 * @date 2019/7/5 22:20
 */
class SessionApplication : MultiDexApplication() {

    override fun onCreate() {
        super.onCreate()
        // 初始化工具类
        ActivityUtil.bindToApplication(this)
        LogUtil.isDebug = true
        LogUtil.tag = javaClass.simpleName
        Repository.initialize(this, true)
    }
}