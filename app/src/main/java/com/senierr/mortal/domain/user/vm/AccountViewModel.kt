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
 * 帐号模块
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class AccountViewModel : ViewModel() {

    private val _loginResult = MutableSharedFlow<StatefulData<UserInfo>>()
    val loginResult: SharedFlow<StatefulData<UserInfo>> = _loginResult

    private val _registerResult = MutableSharedFlow<StatefulData<UserInfo>>()
    val registerResult: SharedFlow<StatefulData<UserInfo>> = _registerResult

    private val _logoutResult = MutableSharedFlow<StatefulData<Boolean>>()
    val logoutResult: SharedFlow<StatefulData<Boolean>> = _logoutResult

    private val _deleteResult = MutableSharedFlow<StatefulData<Boolean>>()
    val deleteResult: SharedFlow<StatefulData<Boolean>> = _deleteResult

    private val _resetPasswordResult = MutableSharedFlow<StatefulData<Boolean>>()
    val resetPasswordResult: SharedFlow<StatefulData<Boolean>> = _resetPasswordResult

    private val userService = Repository.getService<IUserService>()

    /**
     * 登录
     */
    fun login(account: String, password: String) {
        viewModelScope.launch {
            try {
                val userInfo = userService.login(account, password)
                _loginResult.emitSuccess(userInfo)
            } catch (e: Exception) {
                _loginResult.emitFailure(e)
            }
        }
    }

    /**
     * 登录
     */
    fun register(account: String, password: String) {
        viewModelScope.launch {
            try {
                val userInfo = userService.register(account, password)
                _registerResult.emitSuccess(userInfo)
            } catch (e: Exception) {
                _registerResult.emitFailure(e)
            }
        }
    }

    /**
     * 登出
     */
    fun logout(objectId: String) {
        viewModelScope.launch {
            try {
                val result = userService.logout(objectId)
                _logoutResult.emitSuccess(result)
            } catch (e: Exception) {
                _logoutResult.emitFailure(e)
            }
        }
    }

    /**
     * 注销账户
     */
    fun delete(objectId: String, sessionToken: String) {
        viewModelScope.launch {
            try {
                val result = userService.delete(objectId, sessionToken)
                _deleteResult.emitSuccess(result)
            } catch (e: Exception) {
                _deleteResult.emitFailure(e)
            }
        }
    }

    /**
     * 重置密码
     */
    fun resetPassword(userInfo: UserInfo, oldPassword: String, newPassword: String) {
        viewModelScope.launch {
            try {
                val response = userService.resetPassword(
                    userInfo.objectId, userInfo.sessionToken,
                    oldPassword, newPassword
                )
                _resetPasswordResult.emitSuccess(response)
            } catch (e: Exception) {
                _resetPasswordResult.emitFailure(e)
            }
        }
    }
}