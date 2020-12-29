package com.senierr.repository

import android.content.Context
import com.senierr.repository.db.DatabaseManager
import com.senierr.repository.disk.DiskManager
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.progress.ProgressBus
import com.senierr.repository.service.api.ICommonService
import com.senierr.repository.service.api.IGankService
import com.senierr.repository.service.api.ISettingService
import com.senierr.repository.service.api.IUserService
import com.senierr.repository.service.impl.CommonService
import com.senierr.repository.service.impl.GankService
import com.senierr.repository.service.impl.SettingService
import com.senierr.repository.service.impl.UserService
import com.senierr.repository.sp.SPManager

/**
 * 数据仓库
 *
 * @author zhouchunjie
 * @date 2019/7/5 20:17
 */
object Repository {

    /**
     * 初始化
     */
    fun initialize(context: Context, isDebug: Boolean) {
        RemoteManager.initialize(context, isDebug)
        DatabaseManager.initialize(context)
        SPManager.initialize(context)
        DiskManager.initialize(context)
    }

    /**
     * 获取数据服务
     */
    inline fun <reified T> getService(): T = when (T::class.java) {
        IGankService::class.java -> GankService() as T
        IUserService::class.java -> UserService() as T
        ICommonService::class.java -> CommonService() as T
        ISettingService::class.java -> SettingService() as T
        else -> throw IllegalArgumentException("Can not find ${T::class.java.simpleName}!")
    }

    /**
     * 获取进度
     */
    fun getProgressBus(): ProgressBus = ProgressBus
}