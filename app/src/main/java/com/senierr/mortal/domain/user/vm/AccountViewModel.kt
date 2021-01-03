package com.senierr.mortal.domain.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.service.api.IUserService
import kotlinx.coroutines.launch

/**
 * 登录
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class AccountViewModel : ViewModel() {

    val loginResult = StatefulLiveData<UserInfo>()
    val logoutResult = StatefulLiveData<Boolean>()

    private val userService = Repository.getService<IUserService>()

    /**
     * 登录
     */
    fun login(account: String, password: String) {
        viewModelScope.launch {
            try {
                val userInfo = userService.login(account, password)
                loginResult.setValue(userInfo)
            } catch (e: Exception) {
                loginResult.setException(e)
            }
        }
    }

    /**
     * 登出
     */
    fun logout(objectId: String?) {
        viewModelScope.launch {
            try {
                val result = if (objectId == null) {
                    val currentUserInfo = userService.getCacheUserInfo()
                    userService.logout(currentUserInfo.objectId)
                } else {
                    userService.logout(objectId)
                }
                logoutResult.setValue(result)
            } catch (e: Exception) {
                logoutResult.setException(e)
            }
        }
    }
}