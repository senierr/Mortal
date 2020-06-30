package com.senierr.repository.service.impl

import com.senierr.repository.entity.bmob.Advert
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.AdvertApi
import com.senierr.repository.service.api.IAdvertService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author zhouchunjie
 * @date 2020/6/29
 */
class AdvertService : IAdvertService {

    private val advertApi by lazy { RemoteManager.getBmobHttp().create(AdvertApi::class.java) }

    override suspend fun getSplash(): MutableList<Advert> {
        return withContext(Dispatchers.IO) {
            return@withContext advertApi.getSplash().results
        }
    }
}