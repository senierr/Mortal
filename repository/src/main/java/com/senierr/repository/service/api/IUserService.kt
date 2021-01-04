package com.senierr.repository.service.api

import com.senierr.repository.entity.bmob.BmobException
import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.UserInfo

/**
 * 用户服务
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface IUserService {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun login(username: String, password: String): UserInfo

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun register(username: String, password: String): UserInfo

    /**
     * 登出
     */
    suspend fun logout(objectId: String): Boolean

    /**
     * 拉取最新用户信息
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun fetchUserInfo(objectId: String): UserInfo

    /**
     * 获取所有缓存用户信息
     */
    suspend fun getAllCacheUserInfo(): MutableList<UserInfo>

    /**
     * 获取已登录的缓存用户信息
     *
     * @throws NotLoggedException
     */
    suspend fun getLoggedCacheUserInfo(): UserInfo

    /**
     * 删除所有缓存用户信息
     */
    suspend fun clearAllCacheUserInfo()

    /**
     * 删除缓存用户信息
     */
    suspend fun clearCacheUserInfo(objectId: String)

    /**
     * 更新用户信息
     *
     * @param infoMap 需要更新的信息表，例如：<"nickname": "Tom">
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun updateUserInfo(objectId: String, sessionToken: String, infoMap: MutableMap<String, String>): BmobResponse

    /**
     * 重置密码
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun resetPassword(
        objectId: String,
        sessionToken: String,
        oldPassword: String,
        newPassword: String
    ): BmobResponse
}