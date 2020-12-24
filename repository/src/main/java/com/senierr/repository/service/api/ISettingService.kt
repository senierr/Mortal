package com.senierr.repository.service.api

import com.senierr.repository.entity.bmob.VersionInfo

/**
 * 设置服务
 *
 * @author zhouchunjie
 * @date 2020/12/19
 */
interface ISettingService {

    /**
     * 检测新版本
     */
    suspend fun checkNewVersion(applicationId: String): VersionInfo?
}