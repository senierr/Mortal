package com.senierr.mortal.domain.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.dto.UserInfo
import kotlinx.coroutines.launch

/**
 * 登录
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class LoginViewModel : ViewModel() {

    val loginResult = StatefulLiveData<UserInfo>()

//    private val userService = Repository.getService<IUserService>()

    fun login(account: String, password: String) {
//        viewModelScope.launch {
//            try {
//                val userInfo = userService.login(account, password)
//                loginResult.setValue(userInfo)
//            } catch (e: Exception) {
//                loginResult.setException(e)
//            }
//        }
    }
}