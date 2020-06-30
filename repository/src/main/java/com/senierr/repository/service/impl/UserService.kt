package com.senierr.repository.service.impl

import com.senierr.repository.db.DatabaseManager
import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.UserApi
import com.senierr.repository.service.api.IUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *
 * @author zhouchunjie
 * @date 2020/6/20 12:53
 */
class UserService : IUserService {

    private val userApi by lazy { RemoteManager.getBmobHttp().create(UserApi::class.java) }
    private val userInfoDao by lazy { DatabaseManager.getDatabase().getUserInfoDao() }

    override suspend fun login(username: String, password: String): UserInfo {
        return withContext(Dispatchers.IO) {
            val userInfo = userApi.login(username, password)
            userInfoDao.insertOrReplace(userInfo)
            return@withContext userInfo
        }
    }

    override suspend fun getUserInfo(objectId: String): UserInfo {
        return withContext(Dispatchers.IO) {
            val userInfo = userApi.getUserInfo(objectId)
            userInfoDao.insertOrReplace(userInfo)
            return@withContext userInfo
        }
    }

    override suspend fun getCacheUserInfo(): UserInfo? {
        return withContext(Dispatchers.IO) {
            // TODO 多用户需要处理
            return@withContext userInfoDao.getAll().firstOrNull()
        }
    }

    override suspend fun clearCacheUserInfo(objectId: String) {
        return withContext(Dispatchers.IO) {
            return@withContext userInfoDao.deleteById(objectId)
        }
    }

    override suspend fun checkSession(objectId: String, sessionToken: String): BmobResponse {
        return withContext(Dispatchers.IO) {
            return@withContext userApi.checkSession(sessionToken, objectId)
        }
    }

    override suspend fun updateEmail(
        objectId: String,
        sessionToken: String,
        email: String): BmobResponse {
        return withContext(Dispatchers.IO) {
            return@withContext userApi.updateEmail(sessionToken, objectId, email)
        }
    }

    override suspend fun resetPassword(
        objectId: String,
        sessionToken: String,
        oldPassword: String,
        newPassword: String
    ): BmobResponse {
        return withContext(Dispatchers.IO) {
            return@withContext userApi.resetPassword(
                sessionToken,
                objectId,
                oldPassword,
                newPassword
            )
        }
    }
}