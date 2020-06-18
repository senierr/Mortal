package com.senierr.mortal.app

import android.content.Context
import androidx.multidex.MultiDex
import com.senierr.base.support.ui.BaseApplication
import com.senierr.repository.Repository

/**
 *
 * @author zhouchunjie
 * @date 2019/7/5 22:20
 */
class SessionApplication : BaseApplication() {

    override fun isDebug(): Boolean = true

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }

    override fun onCreate() {
        super.onCreate()
        Repository.initialize(this, isDebug())
    }
}