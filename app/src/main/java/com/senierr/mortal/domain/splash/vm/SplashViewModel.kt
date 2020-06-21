package com.senierr.mortal.domain.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.entity.bmob.UserInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 引导页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SplashViewModel : ViewModel() {

    val autoLoginResult = StatefulLiveData<UserInfo>()

//    private val userService = Repository.getService<IUserService>()

    fun autoLogin() {
        viewModelScope.launch {
            try {
//                val response = userService.autoLogin()
                delay(2 * 1000)
//                autoLoginSuccess.value = response
            } catch (e: Exception) {
//                autoLoginFailure.value = e
            }
        }
    }
}