package com.senierr.mortal.domain.splash.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.mortal.worker.SplashWorker
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.Girl
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 引导页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SplashViewModel(application: Application) : AndroidViewModel(application) {

    val randomGil = StatefulLiveData<Girl>()

    private val gankService = Repository.getService<IGankService>()

    /**
     * 获取广告位
     */
    fun fetchAdvert() {
        viewModelScope.launch {
            try {
                // 展示已加载广告位
                val cacheGirl = gankService.getCacheRandomGirls(1).firstOrNull()
                if (cacheGirl != null) {
                    randomGil.setValue(cacheGirl)
                }
                // 启动后台预加载
                SplashWorker.start(getApplication())
            } catch (e: Exception) {
                randomGil.setException(e)
            }
        }
    }
}