package com.senierr.mortal.domain.splash.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.Advert
import com.senierr.repository.service.api.IAdvertService
import kotlinx.coroutines.launch

/**
 * 引导页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SplashViewModel : ViewModel() {

    val fetchAdvertResult = StatefulLiveData<Advert>()

    private val advertService = Repository.getService<IAdvertService>()

    fun fetchAdvert() {
        viewModelScope.launch {
            try {
                val adverts = advertService.getSplash()
                if (adverts.isNotEmpty()) {
                    fetchAdvertResult.setValue(adverts.first())
                }
            } catch (e: Exception) {
                fetchAdvertResult.setException(e)
            }
        }
    }
}