package com.senierr.mortal.domain.setting.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.utils.FileUtil
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.Category
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 设置
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class SettingViewModel(application: Application) : AndroidViewModel(application) {

    val cacheSize = StatefulLiveData<Double>()

    /**
     * 获取缓存大小
     */
    fun getCacheSize() {
        viewModelScope.launch {
            try {
                val context = getApplication<Application>()
                val cacheDirSize = FileUtil.getFileSize(context.cacheDir).toDouble()
                val externalCacheDirSize = FileUtil.getFileSize(context.externalCacheDir).toDouble()
                val totalCacheSize = cacheDirSize + externalCacheDirSize
                cacheSize.setValue(totalCacheSize)
            } catch (e: Exception) {
                cacheSize.setException(e)
            }
        }
    }
}