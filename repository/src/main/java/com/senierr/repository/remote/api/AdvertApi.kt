package com.senierr.repository.remote.api

import com.senierr.repository.entity.bmob.Advert
import com.senierr.repository.entity.bmob.BmobArray
import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.UserInfo
import retrofit2.http.*

/**
 * 广告模块API
 *
 * @author zhouchunjie
 * @date 2020/5/7
 */
interface AdvertApi {

    /**
     * 获取启动页广告
     */
    @GET("1/classes/advert")
    suspend fun getSplash(): BmobArray<Advert>
}