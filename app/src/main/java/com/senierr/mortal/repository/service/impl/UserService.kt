package com.senierr.mortal.repository.service.impl

import com.senierr.mortal.repository.db.DatabaseManager
import com.senierr.mortal.repository.entity.bmob.UserInfo
import com.senierr.mortal.repository.exception.NotLoggedException
import com.senierr.mortal.repository.remote.RemoteManager
import com.senierr.mortal.repository.remote.api.UserApi
import com.senierr.mortal.repository.service.api.IUserService
import com.senierr.mortal.repository.sp.SPManager
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
    private val spUtil by lazy { SPManager.getSP() }

    override suspend fun login(username: String, password: String): UserInfo {
        return withContext(Dispatchers.IO) {
            val userInfo = userApi.login(username, password)
            // 保存密码
            userInfo.password = password
            // 置为登录状态
            userInfo.logged = true
            userInfoDao.insertOrReplace(userInfo)
            return@withContext userInfo
        }
    }

    override suspend fun register(username: String, password: String): UserInfo {
        return withContext(Dispatchers.IO) {
            val userInfo = mutableMapOf<String, String>()
            userInfo["username"] = username
            userInfo["password"] = password
            return@withContext userApi.register(userInfo)
        }
    }

    override suspend fun logout(objectId: String): Boolean {
        return withContext(Dispatchers.IO) {
            val cache = userInfoDao.get(objectId)
            cache?.let {
                it.logged = false
                userInfoDao.insertOrReplace(it)
            }
            return@withContext true
        }
    }

    override suspend fun delete(objectId: String, sessionToken: String): Boolean {
        return withContext(Dispatchers.IO) {
            val response = userApi.delete(sessionToken, objectId)
            userInfoDao.deleteById(objectId)
            return@withContext response.isSuccessful()
        }
    }

    override suspend fun fetchUserInfo(objectId: String): UserInfo {
        return withContext(Dispatchers.IO) {
            val cache = userInfoDao.get(objectId)
            val userInfo = userApi.getUserInfo(objectId)
            cache?.let {
                // 保存密码
                userInfo.password = it.password
                // 保存SessionToken
                userInfo.sessionToken = it.sessionToken
                // 保存登录状态
                userInfo.logged = it.logged
            }
            userInfoDao.insertOrReplace(userInfo)
            return@withContext userInfo
        }
    }

    override suspend fun getAllCacheUserInfo(): MutableList<UserInfo> {
        return withContext(Dispatchers.IO) {
            return@withContext userInfoDao.getAll()
        }
    }

    override suspend fun getLoggedCacheUserInfo(): UserInfo {
        return withContext(Dispatchers.IO) {
            val caches = userInfoDao.getAll()
            val result = caches.firstOrNull {
                return@firstOrNull it.logged
            }
            return@withContext result?: throw NotLoggedException()
        }
    }

    override suspend fun clearAllCacheUserInfo() {
        return withContext(Dispatchers.IO) {
            return@withContext userInfoDao.deleteAll()
        }
    }

    override suspend fun clearCacheUserInfo(objectId: String) {
        return withContext(Dispatchers.IO) {
            return@withContext userInfoDao.deleteById(objectId)
        }
    }

    override suspend fun updateUserInfo(
        objectId: String,
        sessionToken: String,
        infoMap: MutableMap<String, String>
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val response = userApi.updateInfo(sessionToken, objectId, infoMap)
            return@withContext response.isSuccessful()
        }
    }

    override suspend fun resetPassword(
        objectId: String,
        sessionToken: String,
        oldPassword: String,
        newPassword: String
    ): Boolean {
        return withContext(Dispatchers.IO) {
            val response = userApi.resetPassword(
                sessionToken,
                objectId,
                mutableMapOf(
                    Pair("oldPassword", oldPassword),
                    Pair("newPassword", newPassword)
                )
            )
            return@withContext response.isSuccessful()
        }
    }
}