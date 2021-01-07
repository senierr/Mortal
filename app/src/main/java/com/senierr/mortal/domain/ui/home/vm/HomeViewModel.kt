package com.senierr.mortal.domain.ui.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.ui.common.vm.StatefulLiveData
import com.senierr.mortal.repository.Repository
import com.senierr.mortal.repository.entity.gank.Category
import com.senierr.mortal.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class HomeViewModel : ViewModel() {

    val fetchCategoriesResult = StatefulLiveData<MutableList<Category>>()

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取分类
     */
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val categories = gankService.getGanHuoCategories()
                fetchCategoriesResult.setValue(categories)
            } catch (e: Exception) {
                fetchCategoriesResult.setException(e)
            }
        }
    }
}