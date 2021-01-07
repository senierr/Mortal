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
    suspend fun checkNewVersion(): VersionInfo?

    /**
     * 忽略版本更新
     *
     * @param versionName 忽略的版本
     */
    suspend fun ignoreUpdateVersion(versionName: String): Boolean

    /**
     * 获取本地缓存大小
     */
    suspend fun getLocalCacheSize(): Long

    /**
     * 清除本地缓存
     */
    suspend fun clearLocalCache()
}