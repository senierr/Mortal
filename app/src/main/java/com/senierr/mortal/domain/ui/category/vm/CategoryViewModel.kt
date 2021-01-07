package com.senierr.mortal.domain.ui.category.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.ui.common.vm.StatefulLiveData
import com.senierr.mortal.repository.Repository
import com.senierr.mortal.repository.entity.gank.Category
import com.senierr.mortal.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 分类标签
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class CategoryViewModel : ViewModel() {

    val ganHuoCategories = StatefulLiveData<MutableList<Category>>()
    val saveCategories = StatefulLiveData<Boolean>()

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取分类标签
     */
    fun fetchGanHuoCategories() {
        viewModelScope.launch {
            try {
                val categories = gankService.getGanHuoCategories()
                ganHuoCategories.setValue(categories)
            } catch (e: Exception) {
                ganHuoCategories.setException(e)
            }
        }
    }

    /**
     * 保存分类标签
     */
    fun saveGanHuoCategories(categories: MutableList<Category>) {
        viewModelScope.launch {
            try {
                val result = gankService.saveGanHuoCategories(categories)
                saveCategories.setValue(result)
            } catch (e: Exception) {
                saveCategories.setException(e)
            }
        }
    }
}