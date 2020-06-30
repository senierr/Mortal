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
     * 获取用户信息
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun getUserInfo(objectId: String): UserInfo

    /**
     * 获取缓存用户信息
     */
    suspend fun getCacheUserInfo(): UserInfo?

    /**
     * 删除缓存用户信息
     */
    suspend fun clearCacheUserInfo(objectId: String)

    /**
     * 检查用户的登录是否过期
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun checkSession(objectId: String, sessionToken: String): BmobResponse

    /**
     * 更新用户邮箱
     *
     * @throws BmobException 网络请求异常
     */
    suspend fun updateEmail(objectId: String, sessionToken: String, email: String): BmobResponse

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