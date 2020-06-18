package com.senierr.repository.remote.api

import com.senierr.repository.entity.dto.HttpResponse
import com.senierr.repository.entity.dto.UserInfo
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

/**
 * 用户模块API
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface UserApi {

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     */
    @FormUrlEncoded
    @POST("user/login")
    suspend fun login(
        @Field("username") username: String,
        @Field("password") password: String
    ): HttpResponse<UserInfo>

    /**
     * 注册
     *
     * @param username 用户名
     * @param password 密码
     * @param repassword 确认密码
     */
    @FormUrlEncoded
    @POST("user/register")
    suspend fun register(
        @Field("username") username: String,
        @Field("password") password: String,
        @Field("repassword") repassword: String
    ): HttpResponse<UserInfo>

    /**
     * 登出
     */
    @GET("user/logout/json")
    suspend fun logout(): HttpResponse<UserInfo>
}