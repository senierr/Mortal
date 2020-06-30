package com.senierr.mortal.domain.splash.vm

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.Advert
import com.senierr.repository.service.api.IAdvertService
import com.senierr.repository.service.api.IUserService
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * 引导页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SplashViewModel : ViewModel() {

    val fetchAdvertResult = StatefulLiveData<Advert>()
    val asyncStartResult = StatefulLiveData<Unit>()

    private val advertService = Repository.getService<IAdvertService>()
    private val userService = Repository.getService<IUserService>()

    fun asyncStart() {
        viewModelScope.launch {
            val fetchAdvert = async { fetchAdvert() }
            val checkSession = async { checkSession() }
            fetchAdvert.await()
            checkSession.await()
            asyncStartResult.setValue(Unit)
        }
    }

    private suspend fun fetchAdvert() {
        try {
            val adverts = advertService.getSplash()
            if (adverts.isNotEmpty()) {
                fetchAdvertResult.setValue(adverts.first())
            }
        } catch (e: Exception) {
            fetchAdvertResult.setException(e)
        }
    }

    private suspend fun checkSession() {
        try {
            val cacheUserInfo = userService.getCacheUserInfo()
            if (cacheUserInfo != null) {
                val response = userService.checkSession(cacheUserInfo.objectId, cacheUserInfo.sessionToken)
                if (!response.isSuccessful()) {
                    userService.clearCacheUserInfo(cacheUserInfo.objectId)
                }
            }
        } catch (e: Exception) {
            LogUtil.logW(Log.getStackTraceString(e))
        }
    }
}