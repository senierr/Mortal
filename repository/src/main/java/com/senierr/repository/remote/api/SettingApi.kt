package com.senierr.repository.remote.api

import com.senierr.repository.entity.bmob.VersionInfo
import retrofit2.http.POST

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
    @POST("user/login")
    suspend fun checkNewVersion(): VersionInfo
}