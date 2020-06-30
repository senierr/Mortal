package com.senierr.repository.service.api

import com.senierr.repository.entity.bmob.*

/**
 * 广告服务
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface IAdvertService {

    /**
     * 获取启动页广告
     */
    suspend fun getSplash(): MutableList<Advert>
}