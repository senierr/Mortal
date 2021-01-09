package com.senierr.repository.remote.api

import com.senierr.repository.entity.bmob.BmobResponse
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

/**
 * 公共模块API
 *
 * @author zhouchunjie
 * @date 2020/5/26
 */
interface CommonApi {

    /**
     * 上传文件
     *
     * @param url 上传链接
     */
    @POST("../")
    suspend fun uploadFile(@Body requestBody: RequestBody): BmobResponse

    /**
     * 下载文件
     *
     * @param url 下载链接
     */
    @Streaming
    @GET
    fun downloadFile(@Url url: String?): Call<ResponseBody?>
}