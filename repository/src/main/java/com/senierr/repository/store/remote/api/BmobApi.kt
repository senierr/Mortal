package com.senierr.repository.store.remote.api

import com.senierr.repository.entity.bmob.*
import retrofit2.http.*

/**
 * Bmob API
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface BmobApi {

    /**
     * 添加数据
     *
     * @param tableName 表名
     * @param properties 属性集，例如："title":"xxx" 标题
     */
    @POST("1/classes/{tableName}")
    suspend fun insert(
            @Path("tableName") tableName: String,
            @Body properties: MutableMap<String, String>
    ): BmobInsertResponse

    /**
     * 更新数据
     *
     * @param tableName 表名
     * @param objectId 对象ID
     * @param properties 属性集，例如："title":"xxx" 标题
     */
    @PUT("1/classes/{tableName}/{objectId}")
    suspend fun update(
            @Path("tableName") tableName: String,
            @Path("objectId") objectId: String,
            @Body properties: MutableMap<String, String>
    ): BmobUpdateResponse

    /**
     * 原子计算器
     *
     * @param tableName 表名
     * @param objectId 对象ID
     * @param properties 属性集，例如："title":"xxx" 标题
     */
    @PUT("1/classes/{tableName}/{objectId}")
    suspend fun increase(
            @Path("tableName") tableName: String,
            @Path("objectId") objectId: String,
            @Body properties: MutableMap<String, BmobOperation.Increment>
    ): BmobUpdateResponse

    /**
     * 删除数据
     *
     * @param tableName 表名
     * @param objectId 对象ID
     */
    @DELETE("1/classes/{tableName}/{objectId}")
    suspend fun delete(
            @Path("tableName") tableName: String,
            @Path("objectId") objectId: String
    ): BmobDefaultResponse

    /**
     * 删除字段
     *
     * @param tableName 表名
     * @param objectId 对象ID
     */
    @PUT("1/classes/{tableName}/{objectId}")
    suspend fun deleteField(
            @Path("tableName") tableName: String,
            @Path("objectId") objectId: String,
            @Body properties: MutableMap<String, BmobOperation.Delete>
    ): BmobDefaultResponse


    /**
     * 查询单个对象
     *
     * @param tableName 表名
     * @param objectId 对象ID
     * @param include 当获取的对象有指向其子对象的Pointer类型指针Key时，你可以加入inclue选项来获取指针指向的子对象。
     */
    @GET("1/classes/{tableName}/{objectId}")
    suspend fun <T> query(
            @Path("tableName") tableName: String,
            @Path("objectId") objectId: String,
            @Query("include") include: String? = null
    ): T

    /**
     * 查询多个对象
     *
     * @param tableName 表名
     * @param include 当获取的对象有指向其子对象的Pointer类型指针Key时，你可以加入inclue选项来获取指针指向的子对象。
     * @param where 条件
     * @param limit 分页数量
     * @param count 结果计数
     * @param skip 跳过数量
     * @param order 排序，例如：score（升序）；-score（降序）；score,-name（多个排序）；
     */
    @GET("1/classes/{tableName}")
    suspend fun <T> query(
            @Path("tableName") tableName: String,
            @Query("include") include: String? = null,
            @Query("where") where: BmobWhere? = null,
            @Query("limit") limit: Int? = null,
            @Query("count") count: Int? = null,
            @Query("skip") skip: Int? = null,
            @Query("order") order: String? = null
    ): BmobArrayResponse<T>
}