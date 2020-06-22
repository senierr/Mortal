package com.senierr.repository.service.impl

import com.senierr.repository.db.DatabaseManager
import com.senierr.repository.entity.bmob.BmobResponse
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.exception.NotLoggedException
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

    override suspend fun getCacheUserInfo(): UserInfo {
        return withContext(Dispatchers.IO) {
            return@withContext userInfoDao.getAll().firstOrNull()?: throw NotLoggedException()
        }
    }

    override suspend fun checkSession(objectId: String): BmobResponse {
        return withContext(Dispatchers.IO) {
            val userInfo = userInfoDao.getAll().firstOrNull()?: throw NotLoggedException()
            return@withContext userApi.checkSession(userInfo.sessionToken, objectId)
        }
    }

    override suspend fun updateEmail(objectId: String, email: String): BmobResponse {
        return withContext(Dispatchers.IO) {
            val userInfo = userInfoDao.getAll().firstOrNull()?: throw NotLoggedException()
            return@withContext userApi.updateEmail(userInfo.sessionToken, objectId, email)
        }
    }

    override suspend fun resetPassword(
        objectId: String,
        oldPassword: String,
        newPassword: String
    ): BmobResponse {
        return withContext(Dispatchers.IO) {
            val userInfo = userInfoDao.getAll().firstOrNull()?: throw NotLoggedException()
            return@withContext userApi.resetPassword(
                userInfo.sessionToken,
                objectId,
                oldPassword,
                newPassword
            )
        }
    }
}