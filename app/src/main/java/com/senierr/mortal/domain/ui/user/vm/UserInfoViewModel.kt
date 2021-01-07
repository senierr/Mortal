package com.senierr.mortal.domain.ui.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.ui.common.vm.StatefulLiveData
import com.senierr.mortal.repository.entity.bmob.UserInfo
import com.senierr.mortal.repository.exception.NotLoggedException
import com.senierr.mortal.repository.store.db.DatabaseManager
import com.senierr.mortal.repository.store.remote.RemoteManager
import com.senierr.mortal.repository.store.remote.api.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class UserInfoViewModel : ViewModel() {

    val allCacheUserInfo = StatefulLiveData<MutableList<UserInfo>>()
    val loggedCacheUserInfo = StatefulLiveData<UserInfo>()
    val userinfo = StatefulLiveData<UserInfo>()

    private val userApi by lazy { RemoteManager.getBmobHttp().create(UserApi::class.java) }
    private val userInfoDao by lazy { DatabaseManager.getDatabase().getUserInfoDao() }

    /**
     * 获取所有缓存用户信息
     */
    fun getAllCacheUserInfo() {
        viewModelScope.launch {
            try {
                val caches = withContext(Dispatchers.IO) {
                    return@withContext userInfoDao.getAll()
                }
                allCacheUserInfo.setValue(caches)
            } catch (e: Exception) {
                allCacheUserInfo.setException(e)
            }
        }
    }

    /**
     * 获取当前缓存用户信息
     */
    fun getLoggedCacheUserInfo() {
        viewModelScope.launch {
            try {
                val cacheUserInfo = withContext(Dispatchers.IO) {
                    val caches = userInfoDao.getAll()
                    val result = caches.firstOrNull {
                        return@firstOrNull it.logged
                    }
                    return@withContext result?: throw NotLoggedException()
                }
                loggedCacheUserInfo.setValue(cacheUserInfo)
            } catch (e: Exception) {
                loggedCacheUserInfo.setException(e)
            }
        }
    }

    /**
     * 拉取最新用户信息
     */
    fun fetchUserInfo(objectId: String) {
        viewModelScope.launch {
            try {
                val userInfo = withContext(Dispatchers.IO) {
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
                userinfo.setValue(userInfo)
            } catch (e: Exception) {
                userinfo.setException(e)
            }
        }
    }

    /**
     * 更新用户昵称
     */
    fun updateNickname(userInfo: UserInfo, newNickname: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val response = userApi.updateInfo(
                        userInfo.objectId,
                        userInfo.sessionToken,
                        mutableMapOf(Pair("nickname", newNickname))
                    )
                    return@withContext response.isSuccessful()
                }
                if (result) {
                    userInfo.nickname = newNickname
                    userinfo.setValue(userInfo)
                }
            } catch (e: Exception) {
                userinfo.setException(e)
            }
        }
    }

    /**
     * 更新用户邮箱
     */
    fun updateEmail(userInfo: UserInfo, newEmail: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val response = userApi.updateInfo(
                        userInfo.objectId,
                        userInfo.sessionToken,
                        mutableMapOf(Pair("email", newEmail))
                    )
                    return@withContext response.isSuccessful()
                }
                if (result) {
                    userInfo.email = newEmail
                    userinfo.setValue(userInfo)
                }
            } catch (e: Exception) {
                userinfo.setException(e)
            }
        }
    }
}