package com.senierr.repository.service.api

import com.senierr.repository.entity.bmob.BmobException
import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.exception.NotLoggedException
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
     *
     * @throws NotLoggedException 未登录异常
     */
    suspend fun getCacheUserInfo(): UserInfo

    /**
     * 检查用户的登录是否过期
     *
     * @throws NotLoggedException 未登录异常
     * @throws BmobException 网络请求异常
     */
    suspend fun checkSession(objectId: String): BmobResponse

    /**
     * 更新用户邮箱
     *
     * @throws NotLoggedException 未登录异常
     * @throws BmobException 网络请求异常
     */
    suspend fun updateEmail(objectId: String, email: String): BmobResponse

    /**
     * 重置密码
     *
     * @throws NotLoggedException 未登录异常
     * @throws BmobException 网络请求异常
     */
    suspend fun resetPassword(
        objectId: String,
        oldPassword: String,
        newPassword: String
    ): BmobResponse
}