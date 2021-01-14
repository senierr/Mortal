package com.senierr.mortal.domain.splash.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.mortal.worker.SplashWorker
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.Girl
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * 引导页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SplashViewModel(application: Application) : AndroidViewModel(application) {

    private val _randomGil = MutableSharedFlow<StatefulData<Girl>>()
    val randomGil: SharedFlow<StatefulData<Girl>> = _randomGil

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
                    _randomGil.emitSuccess(cacheGirl)
                }
                // 启动后台预加载
                SplashWorker.start(getApplication())
            } catch (e: Exception) {
                _randomGil.emitFailure(e)
            }
        }
    }
}