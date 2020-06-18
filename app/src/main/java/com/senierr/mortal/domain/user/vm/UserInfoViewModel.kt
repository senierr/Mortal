package com.senierr.mortal.domain.user.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.repository.Repository
import com.senierr.repository.entity.dto.UserInfo
import kotlinx.coroutines.launch

/**
 * 用户详情
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class UserInfoViewModel : ViewModel() {

    val fetchUserInfoSuccess = MutableLiveData<UserInfo>()
    val fetchUserInfoFailure = MutableLiveData<Exception>()

//    private val userService = Repository.getService<IUserService>()

    fun fetchUserInfo() {
        viewModelScope.launch {
//            try {
//                val userInfo = userService.getUserInfo()
//                fetchUserInfoSuccess.value = userInfo
//            } catch (e: Exception) {
//                fetchUserInfoFailure.value = e
//            }
        }
    }
}