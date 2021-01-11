package com.senierr.repository.service.api

import com.senierr.repository.entity.bmob.ViewHistory
import com.senierr.repository.entity.gank.GanHuo

/**
 * 文章服务
 *
 * @author zhouchunjie
 * @date 2020/12/19
 */
interface IArticleService {

    /**
     * 发送浏览记录
     */
    suspend fun sendViewHistory(userId: String, articleId: String, articleTitle: String, articleUrl: String): ViewHistory

    /**
     * 获取浏览记录
     *
     * @param userId 用户ID
     * @param page 页数 > 0
     * @param count 每页数据数量 > 0
     */
    suspend fun getViewHistories(userId: String, page: Int, count: Int): MutableList<ViewHistory>

    /**
     * 删除浏览记录
     */
    suspend fun deleteViewHistory(objectId: String): Boolean
}