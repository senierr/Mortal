package com.senierr.repository.remote.api

import com.senierr.repository.entity.bmob.BmobArray
import com.senierr.repository.entity.bmob.VersionInfo
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

/**
 * 设置模块API
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface SettingApi {

    /**
     * 检测新版本
     */
    @GET("1/classes/version_info")
    suspend fun checkNewVersion(@Query("where") where: String): BmobArray<VersionInfo>
}