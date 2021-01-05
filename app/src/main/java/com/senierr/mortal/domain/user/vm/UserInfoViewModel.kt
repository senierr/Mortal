package com.senierr.mortal.domain.user.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.service.api.IUserService
import kotlinx.coroutines.launch

/**
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class UserInfoViewModel : ViewModel() {

    val allCacheUserInfo = StatefulLiveData<MutableList<UserInfo>>()
    val loggedCacheUserInfo = StatefulLiveData<UserInfo>()
    val userinfo = StatefulLiveData<UserInfo>()

    private val userService = Repository.getService<IUserService>()

    /**
     * 获取所有缓存用户信息
     */
    fun getAllCacheUserInfo() {
        viewModelScope.launch {
            try {
                val caches = userService.getAllCacheUserInfo()
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
                val cacheUserInfo = userService.getLoggedCacheUserInfo()
                loggedCacheUserInfo.setValue(cacheUserInfo)
            } catch (e: Exception) {
                LogUtil.logE(Log.getStackTraceString(e))
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
                val userInfo = userService.fetchUserInfo(objectId)
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
                userService.updateUserInfo(
                    userInfo.objectId,
                    userInfo.sessionToken,
                    mutableMapOf(Pair("nickname", newNickname))
                )
                userInfo.nickname = newNickname
                userinfo.setValue(userInfo)
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
                userService.updateUserInfo(
                    userInfo.objectId,
                    userInfo.sessionToken,
                    mutableMapOf(Pair("email", newEmail))
                )
                userInfo.email = newEmail
                userinfo.setValue(userInfo)
            } catch (e: Exception) {
                userinfo.setException(e)
            }
        }
    }
}