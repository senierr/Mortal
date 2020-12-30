package com.senierr.mortal.domain.setting.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.Category
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 分类标签
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class CategoryViewModel : ViewModel() {

    val cacheCategories = StatefulLiveData<MutableList<Category>>()
    val remoteCategories = StatefulLiveData<MutableList<Category>>()

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取缓存分类标签
     */
    fun fetchCacheCategories() {
        viewModelScope.launch {
            try {
                val categories = gankService.getGanHuoCacheCategories()
                cacheCategories.setValue(categories)
            } catch (e: Exception) {
                cacheCategories.setException(e)
            }
        }
    }

    /**
     * 拉取分类
     */
    fun fetchRemoteCategories() {
        viewModelScope.launch {
            try {
                val categories = gankService.getGanHuoCategories()
                remoteCategories.setValue(categories)
            } catch (e: Exception) {
                remoteCategories.setException(e)
            }
        }
    }
}