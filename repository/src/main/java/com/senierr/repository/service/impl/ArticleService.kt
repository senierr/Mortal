package com.senierr.repository.service.impl

import com.google.gson.Gson
import com.senierr.repository.entity.bmob.ViewHistory
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.ArticleApi
import com.senierr.repository.service.api.IArticleService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author chunjiezhou
 * @date 2021/01/11
 */
class ArticleService : IArticleService {

    private val articleApi by lazy { RemoteManager.getBmobHttp().create(ArticleApi::class.java) }

    override suspend fun sendViewHistory(userId: String, articleId: String, articleTitle: String, articleUrl: String): ViewHistory {
        return withContext(Dispatchers.IO) {
            val response = articleApi.sendViewHistory(mutableMapOf(
                Pair("articleId", articleId),
                Pair("articleTitle", articleTitle),
                Pair("articleUrl", articleUrl),
                Pair("userId", userId)
            ))
            response.articleId = articleId
            response.articleTitle = articleTitle
            response.articleUrl = articleUrl
            response.userId = userId
            return@withContext response
        }
    }

    override suspend fun getViewHistories(userId: String, page: Int, count: Int): MutableList<ViewHistory> {
        return withContext(Dispatchers.IO) {
            val where = HashMap<String, String>()
            where["userId"] = userId
            return@withContext articleApi.getViewHistories(
                Gson().toJson(where), count, count * page,
                "-createdAt"
            ).results
        }
    }

    override suspend fun deleteViewHistory(objectId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = articleApi.deleteViewHistory(objectId)
            return@withContext response.isSuccessful()
        }
    }
}