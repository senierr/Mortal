package com.senierr.repository.service.api

import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.UserInfo
import retrofit2.http.*

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
     */
    suspend fun login(username: String, password: String): UserInfo

    /**
     * 获取用户信息
     */
    suspend fun getUserInfo(objectId: String): UserInfo

    /**
     * 检查用户的登录是否过期
     */
    suspend fun checkSession(sessionToken: String, objectId: String): BmobResponse

    /**
     * 更新用户邮箱
     */
    suspend fun updateEmail(sessionToken: String, objectId: String, email: String): BmobResponse

    /**
     * 重置密码
     */
    suspend fun resetPassword(
        sessionToken: String,
        objectId: String,
        oldPassword: String,
        newPassword: String
    ): BmobResponse
}