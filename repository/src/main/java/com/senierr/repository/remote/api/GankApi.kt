package com.senierr.repository.remote.api

import com.senierr.repository.entity.dto.gank.*
import retrofit2.http.GET
import retrofit2.http.Path

/**
 * 干货集中营API
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface GankApi {

    /**
     * 获取首页轮播
     */
    @GET("banners")
    suspend fun getBanners(): GankResponse<MutableList<Banner>>

    /**
     * 获取随机妹纸图
     *
     * @param count 数量 [10, 50]
     */
    @GET("random/category/Girl/type/Girl/count/{count}")
    suspend fun getRandomGirls(@Path("count") count: Int): GankResponse<MutableList<Girl>>

    /**
     * 获取热门干货
     *
     * @param count 数量 [10, 50]
     */
    @GET("hot/views/category/GanHuo/count/{count}")
    suspend fun getHotGanHuos(@Path("count") count: Int): GankResponse<MutableList<GanHuo>>

    /**
     * 获取妹纸图
     *
     * @param page 页码 >=1
     * @param count 数量 [10, 50]
     */
    @GET("data/category/Girl/type/Girl/page/{page}/count/{count}")
    suspend fun getGirls(
        @Path("page") page: Int,
        @Path("count") count: Int
    ): GankResponse<MutableList<Girl>>

    /**
     * 获取干货下分类
     */
    @GET("categories/GanHuo")
    suspend fun getGanHuoCategories(): GankResponse<MutableList<Category>>

    /**
     * 获取干货
     *
     * @param type 子分类
     * @param page 页码 >=1
     * @param count 数量 [10, 50]
     */
    @GET("data/category/GanHuo/type/{type}/page/{page}/count/{count}")
    suspend fun getGanHuos(
        @Path("type", encoded = true) type: String,
        @Path("page") page: Int,
        @Path("count") count: Int
    ): GankResponse<MutableList<GanHuo>>
}