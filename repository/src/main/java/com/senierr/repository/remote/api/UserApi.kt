package com.senierr.repository.remote.api

import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.UserInfo
import retrofit2.http.*

/**
 * 用户模块API
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface UserApi {

    /**
     * 注册
     *
     * @param userInfo  :username 用户名
     *                  :password 密码
     */
    @POST("1/users")
    suspend fun register(@Body userInfo: MutableMap<String, String>): UserInfo

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @GET("1/login")
    suspend fun login(
        @Query("username") username: String,
        @Query("password") password: String
    ): UserInfo

    /**
     * 获取用户信息
     */
    @GET("1/users/{objectId}")
    suspend fun getUserInfo(@Path("objectId") objectId: String): UserInfo

    /**
     * 检查用户的登录是否过期
     */
    @GET("1/checkSession/{objectId}")
    suspend fun checkSession(
        @Header("X-Bmob-Session-Token") sessionToken: String,
        @Path("objectId") objectId: String
    ): BmobResponse

    /**
     * 更新用户信息
     *
     * @param newInfo eg: <"nickname": "Tom">
     */
    @PUT("1/users/{objectId}")
    suspend fun updateInfo(
        @Header("X-Bmob-Session-Token") sessionToken: String,
        @Path("objectId") objectId: String,
        @Body newInfo: MutableMap<String, String>
    ): BmobResponse

    /**
     * 重置密码
     */
    @PUT("1/updateUserPassword/{objectId}")
    suspend fun resetPassword(
        @Header("X-Bmob-Session-Token") sessionToken: String,
        @Path("objectId") objectId: String,
        @Body oldPassword: String,
        @Body newPassword: String
    ): BmobResponse
}