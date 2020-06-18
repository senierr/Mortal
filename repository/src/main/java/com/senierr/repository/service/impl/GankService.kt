package com.senierr.repository.service.impl

import com.senierr.repository.entity.dto.gank.Banner
import com.senierr.repository.entity.dto.gank.Category
import com.senierr.repository.entity.dto.gank.GanHuo
import com.senierr.repository.entity.dto.gank.Girl
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.GankApi
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author zhouchunjie
 * @date 2020/5/15
 */
class GankService : IGankService {

    private val gankApi by lazy { RemoteManager.getGankHttp().create(GankApi::class.java) }

    override suspend fun getBanners(): MutableList<Banner> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.getBanners()
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }

    override suspend fun getRandomGirls(count: Int): MutableList<Girl> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.getRandomGirls(count)
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }

    override suspend fun getHotGanHuos(count: Int): MutableList<GanHuo> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.getHotGanHuos(count)
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }

    override suspend fun getGirls(page: Int, count: Int): MutableList<Girl> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.getGirls(page, count)
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }

    override suspend fun getGanHuoCategories(): MutableList<Category> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.getGanHuoCategories()
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }

    override suspend fun getGanHuos(type: String, page: Int, count: Int): MutableList<GanHuo> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.getGanHuos(type, page, count)
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }
}