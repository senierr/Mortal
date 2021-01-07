package com.senierr.mortal.domain.ui.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.ui.common.vm.StatefulLiveData
import com.senierr.mortal.repository.entity.bmob.UserInfo
import com.senierr.mortal.repository.store.db.DatabaseManager
import com.senierr.mortal.repository.store.remote.RemoteManager
import com.senierr.mortal.repository.store.remote.api.UserApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 登录
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class AccountViewModel : ViewModel() {

    val loginResult = StatefulLiveData<UserInfo>()
    val registerResult = StatefulLiveData<UserInfo>()
    val logoutResult = StatefulLiveData<Boolean>()
    val deleteResult = StatefulLiveData<Boolean>()
    val resetPasswordResult = StatefulLiveData<Boolean>()

    private val userApi by lazy { RemoteManager.getBmobHttp().create(UserApi::class.java) }
    private val userInfoDao by lazy { DatabaseManager.getDatabase().getUserInfoDao() }

    /**
     * 登录
     */
    fun login(account: String, password: String) {
        viewModelScope.launch {
            try {
                val userInfo = withContext(Dispatchers.IO) {
                    val userInfo = userApi.login(account, password)
                    // 保存密码
                    userInfo.password = password
                    // 置为登录状态
                    userInfo.logged = true
                    userInfoDao.insertOrReplace(userInfo)
                    return@withContext userInfo
                }
                loginResult.setValue(userInfo)
            } catch (e: Exception) {
                loginResult.setException(e)
            }
        }
    }

    /**
     * 登录
     */
    fun register(account: String, password: String) {
        viewModelScope.launch {
            try {
                val userInfo = withContext(Dispatchers.IO) {
                    return@withContext userApi.register(
                        mutableMapOf(
                            Pair("username", account),
                            Pair("password", password)
                        )
                    )
                }
                registerResult.setValue(userInfo)
            } catch (e: Exception) {
                registerResult.setException(e)
            }
        }
    }

    /**
     * 登出
     */
    fun logout(objectId: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val cache = userInfoDao.get(objectId)
                    cache?.let {
                        it.logged = false
                        userInfoDao.insertOrReplace(it)
                    }
                    return@withContext true
                }
                logoutResult.setValue(result)
            } catch (e: Exception) {
                logoutResult.setException(e)
            }
        }
    }

    /**
     * 注销账户
     */
    fun delete(objectId: String, sessionToken: String) {
        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    val response = userApi.delete(sessionToken, objectId)
                    userInfoDao.deleteById(objectId)
                    return@withContext response.isSuccessful()
                }
                deleteResult.setValue(result)
            } catch (e: Exception) {
                deleteResult.setException(e)
            }
        }
    }

    /**
     * 重置密码
     */
    fun resetPassword(userInfo: UserInfo, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    val response = userApi.resetPassword(
                        userInfo.sessionToken,
                        userInfo.objectId,
                        mutableMapOf(
                            Pair("oldPassword", oldPassword),
                            Pair("newPassword", newPassword)
                        )
                    )
                    return@withContext response.isSuccessful()
                }
                resetPasswordResult.setValue(response)
            } catch (e: Exception) {
                resetPasswordResult.setException(e)
            }
        }
    }
}