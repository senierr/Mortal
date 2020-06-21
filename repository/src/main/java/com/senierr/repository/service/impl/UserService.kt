package com.senierr.repository.service.impl

import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.remote.RemoteManager
import com.senierr.repository.remote.api.UserApi
import com.senierr.repository.service.api.IUserService

/**
 *
 * @author zhouchunjie
 * @date 2020/6/20 12:53
 */
class UserService : IUserService {

    private val userApi by lazy { RemoteManager.getBmobHttp().create(UserApi::class.java) }

    override suspend fun login(username: String, password: String): UserInfo {
        return kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            return@withContext userApi.login(username, password)
        }
    }
}