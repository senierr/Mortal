package com.senierr.repository

import android.app.Application
import com.senierr.repository.db.DatabaseManager
import com.senierr.repository.disk.DiskManager
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.service.api.*
import com.senierr.repository.service.impl.*
import com.senierr.repository.sp.SPManager

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
        IUserService::class.java -> UserService() as T
        ICommonService::class.java -> CommonService() as T
        ISettingService::class.java -> SettingService() as T
        IArticleService::class.java -> ArticleService() as T
        else -> throw IllegalArgumentException("Can not find ${T::class.java.simpleName}!")
    }
}