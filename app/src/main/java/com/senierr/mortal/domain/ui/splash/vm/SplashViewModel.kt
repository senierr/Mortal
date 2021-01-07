package com.senierr.mortal.domain.ui.splash.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.ui.common.vm.StatefulLiveData
import com.senierr.mortal.domain.worker.SplashWorker
import com.senierr.mortal.repository.Repository
import com.senierr.mortal.repository.entity.gank.Girl
import com.senierr.mortal.repository.service.api.IGankService
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