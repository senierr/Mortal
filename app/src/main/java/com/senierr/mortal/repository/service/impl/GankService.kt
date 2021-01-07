package com.senierr.mortal.repository.service.impl

import com.google.gson.Gson
import com.senierr.mortal.support.utils.TypeUtil
import com.senierr.mortal.repository.store.disk.DiskLruKey
import com.senierr.mortal.repository.store.disk.DiskManager
import com.senierr.mortal.repository.entity.gank.*
import com.senierr.mortal.repository.store.remote.RemoteManager
import com.senierr.mortal.repository.store.remote.api.GankApi
import com.senierr.mortal.repository.service.api.IGankService
import com.senierr.mortal.repository.store.sp.SPKey
import com.senierr.mortal.repository.store.sp.SPManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author zhouchunjie
 * @date 2020/5/15
 */
class GankService : IGankService {

    private val gankApi by lazy { RemoteManager.getGankHttp().create(GankApi::class.java) }
    private val spUtil by lazy { SPManager.getSP() }
    private val diskLruCache by lazy { DiskManager.getDiskLruCache() }

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
            // 缓存
            diskLruCache?.putString(DiskLruKey.KEY_RANDOM_GIRLS, Gson().toJson(response.data))
            return@withContext response.data
        }
    }

    override suspend fun getCacheRandomGirls(count: Int): MutableList<Girl> {
        return withContext(Dispatchers.IO) {
            val result = mutableListOf<Girl>()
            val cache = diskLruCache?.getString(DiskLruKey.KEY_RANDOM_GIRLS)
            if (cache != null) {
                val girls: MutableList<Girl> = Gson().fromJson(
                    cache,
                    TypeUtil.parseType(MutableList::class.java, arrayOf(Girl::class.java))
                )
                result.addAll(girls)
            }
            return@withContext result
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
            val result = mutableListOf<Category>()
            val cache = spUtil.getString(SPKey.GANHUO_CATEGORY)
            if (cache.isNotBlank()) {
                val categories: MutableList<Category> = Gson().fromJson(
                        cache,
                        TypeUtil.parseType(MutableList::class.java, arrayOf(Category::class.java))
                )
                result.addAll(categories)
            }
            // 若缓存为空，强制从远程获取
            if (result.isEmpty()) {
                val response = gankApi.getGanHuoCategories()
                if (!response.isSuccessful()) throw response.getException()
                result.addAll(response.data)
            }
            return@withContext result
        }
    }

    override suspend fun saveGanHuoCategories(categories: MutableList<Category>): Boolean {
        return withContext(Dispatchers.IO) {
            spUtil.putString(SPKey.GANHUO_CATEGORY, Gson().toJson(categories))
            return@withContext true
        }
    }

    override suspend fun getGanHuos(type: String, page: Int, count: Int): MutableList<GanHuo> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.getGanHuos(type, page, count)
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }

    override suspend fun searchGanHuo(
        search: String,
        type: String,
        page: Int,
        count: Int
    ): MutableList<GanHuo> {
        return withContext(Dispatchers.IO) {
            val response = gankApi.searchGanHuo(search, type, page, count)
            if (!response.isSuccessful()) throw response.getException()
            return@withContext response.data
        }
    }
}