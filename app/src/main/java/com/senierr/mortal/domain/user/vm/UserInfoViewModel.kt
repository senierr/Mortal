package com.senierr.mortal.domain.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.service.api.IUserService
import kotlinx.coroutines.launch

/**
 * 用户详情
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class UserInfoViewModel : ViewModel() {

    val fetchUserInfoResult = StatefulLiveData<UserInfo>()

    private val userService = Repository.getService<IUserService>()

    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                // 先获取缓存数据
                val cacheUserInfo = userService.getCacheUserInfo()
                fetchUserInfoResult.setValue(cacheUserInfo)
                // 再获取最新数据
                val userInfo = userService.getUserInfo(cacheUserInfo.objectId)
                fetchUserInfoResult.setValue(userInfo)
            } catch (e: Exception) {
                fetchUserInfoResult.setException(e)
            }
        }
    }
}