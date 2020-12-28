package com.senierr.mortal.domain.user.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.BmobResponse
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

    val userinfo = StatefulLiveData<UserInfo>()

    private val userService = Repository.getService<IUserService>()

    /**
     * 拉取最新用户信息
     */
    fun fetchUserInfo() {
        viewModelScope.launch {
            try {
                // 先获取缓存数据
                val cacheUserInfo = userService.getCacheUserInfo()
                userinfo.setValue(cacheUserInfo)
                // 再获取最新数据
                val userInfo = userService.getUserInfo(cacheUserInfo.objectId)
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
                userService.updateInfo(
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
                userService.updateInfo(
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