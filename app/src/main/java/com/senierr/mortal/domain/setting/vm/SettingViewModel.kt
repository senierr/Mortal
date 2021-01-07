package com.senierr.mortal.domain.setting.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.service.api.ISettingService
import kotlinx.coroutines.launch

/**
 * 设置
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SettingViewModel(application: Application) : AndroidViewModel(application) {

    val cacheSize = StatefulLiveData<Long>()

    private val settingService = Repository.getService<ISettingService>()

    /**
     * 获取缓存大小
     */
    fun getCacheSize() {
        viewModelScope.launch {
            try {
                cacheSize.setValue(settingService.getLocalCacheSize())
            } catch (e: Exception) {
                cacheSize.setException(e)
            }
        }
    }

    /**
     * 清除缓存
     */
    fun clearCache() {
        viewModelScope.launch {
            try {
                settingService.clearLocalCache()
                cacheSize.setValue(settingService.getLocalCacheSize())
            } catch (e: Exception) {
                cacheSize.setException(e)
            }
        }
    }
}