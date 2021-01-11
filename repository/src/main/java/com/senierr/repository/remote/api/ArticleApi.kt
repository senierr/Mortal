package com.senierr.repository.remote.api

import com.senierr.repository.entity.bmob.BmobArray
import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.Pointer
import com.senierr.repository.entity.bmob.ViewHistory
import retrofit2.http.*

/**
 * 文章模块API
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface ArticleApi {

    /**
     * 发送浏览记录
     *
     * @param viewHistory   "article": Pointer() 文章
     *                      "user": Pointer() 用户
     */
    @POST("1/classes/view_history")
    suspend fun sendViewHistory(@Body viewHistory: MutableMap<String, Pointer>): ViewHistory

    /**
     * 获取浏览记录
     *
     * @param where {"userId":"xxx"} 用户ID
     * @param limit 分页数量
     * @param skip 跳过数量
     */
    @GET("1/classes/view_history")
    suspend fun getViewHistories(
        @Query("where") where: String,
        @Query("limit") limit: Int,
        @Query("skip") skip: Int,
        @Query("order") order: String
    ): BmobArray<ViewHistory>

    /**
     * 删除浏览记录
     */
    @DELETE("1/classes/view_history/{objectId}")
    suspend fun deleteViewHistory(@Path("objectId") objectId: String): BmobResponse
}