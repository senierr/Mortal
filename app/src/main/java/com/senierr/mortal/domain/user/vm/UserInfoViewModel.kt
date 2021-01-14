package com.senierr.mortal.domain.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.service.api.IUserService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class UserInfoViewModel : ViewModel() {

    private val _allCacheUserInfo = MutableSharedFlow<StatefulData<MutableList<UserInfo>>>()
    val allCacheUserInfo: SharedFlow<StatefulData<MutableList<UserInfo>>> = _allCacheUserInfo

    private val _loggedCacheUserInfo = MutableSharedFlow<StatefulData<UserInfo>>()
    val loggedCacheUserInfo: SharedFlow<StatefulData<UserInfo>> = _loggedCacheUserInfo

    private val _userInfo = MutableSharedFlow<StatefulData<UserInfo>>()
    val userInfo: SharedFlow<StatefulData<UserInfo>> = _userInfo

    private val userService = Repository.getService<IUserService>()

    /**
     * 获取所有缓存用户信息
     */
    fun getAllCacheUserInfo() {
        viewModelScope.launch {
            try {
                val caches = userService.getAllCacheUserInfo()
                _allCacheUserInfo.emitSuccess(caches)
            } catch (e: Exception) {
                _allCacheUserInfo.emitFailure(e)
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
                _loggedCacheUserInfo.emitSuccess(cacheUserInfo)
            } catch (e: Exception) {
                _loggedCacheUserInfo.emitFailure(e)
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
                _userInfo.emitSuccess(userInfo)
            } catch (e: Exception) {
                _userInfo.emitFailure(e)
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
                _userInfo.emitSuccess(userInfo)
            } catch (e: Exception) {
                _userInfo.emitFailure(e)
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
                _userInfo.emitSuccess(userInfo)
            } catch (e: Exception) {
                _userInfo.emitFailure(e)
            }
        }
    }
}