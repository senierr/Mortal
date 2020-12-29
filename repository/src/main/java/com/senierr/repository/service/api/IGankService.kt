package com.senierr.repository.service.api

import com.senierr.repository.entity.gank.*

/**
 * 文章服务
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface IGankService {

    /**
     * 获取首页轮播
     */
    suspend fun getBanners(): MutableList<Banner>

    /**
     * 获取随机妹纸图
     *
     * @param count 数量 [10, 50]
     */
    suspend fun getRandomGirls(count: Int): MutableList<Girl>

    /**
     * 获取缓存随机妹纸图
     *
     * @param count 数量 [10, 50]
     */
    suspend fun getCacheRandomGirls(count: Int): MutableList<Girl>

    /**
     * 获取热门干货
     *
     * @param count 数量 [10, 50]
     */
    suspend fun getHotGanHuos(count: Int): MutableList<GanHuo>

    /**
     * 获取妹纸图
     *
     * @param page 页码 >=1
     * @param count 数量 [10, 50]
     */
    suspend fun getGirls(page: Int, count: Int): MutableList<Girl>

    /**
     * 获取干货下分类
     */
    suspend fun getGanHuoCategories(): MutableList<Category>

    /**
     * 获取干货
     *
     * @param type 子分类
     * @param page 页码 >=1
     * @param count 数量 [10, 50]
     */
    suspend fun getGanHuos(type: String, page: Int, count: Int): MutableList<GanHuo>

    /**
     * 搜索干货
     *
     * @param type 子分类
     * @param page 页码 >=1
     * @param count 数量 [10, 50]
     */
    suspend fun searchGanHuo(search: String, type: String, page: Int, count: Int): MutableList<GanHuo>
}