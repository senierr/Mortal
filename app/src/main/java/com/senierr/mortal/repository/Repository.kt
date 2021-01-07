package com.senierr.mortal.repository

import android.app.Application
import com.senierr.mortal.repository.store.db.DatabaseManager
import com.senierr.mortal.repository.store.disk.DiskManager
import com.senierr.mortal.repository.store.remote.RemoteManager
import com.senierr.mortal.repository.store.remote.progress.ProgressBus
import com.senierr.mortal.repository.service.api.IGankService
import com.senierr.mortal.repository.service.impl.GankService
import com.senierr.mortal.repository.store.sp.SPManager

/**
 * 数据仓库
 *
 * @author zhouchunjie
 * @date 2019/7/5 20:17
 */
object Repository {

    private lateinit var application: Application

    /**
     * 初始化
     */
    fun initialize(application: Application, isDebug: Boolean) {
        this.application = application
        RemoteManager.initialize(application, isDebug)
        DatabaseManager.initialize(application)
        SPManager.initialize(application)
        DiskManager.initialize(application)
    }

    /**
     * 获取应用实例
     */
    fun getApplication(): Application = application

    /**
     * 获取数据服务
     */
    inline fun <reified T> getService(): T = when (T::class.java) {
        IGankService::class.java -> GankService() as T
        else -> throw IllegalArgumentException("Can not find ${T::class.java.simpleName}!")
    }

    /**
     * 获取进度
     */
    fun getProgressBus(): ProgressBus = ProgressBus
}