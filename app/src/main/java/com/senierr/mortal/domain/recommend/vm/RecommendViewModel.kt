package com.senierr.mortal.domain.recommend.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.dto.gank.Category
import com.senierr.repository.entity.dto.gank.GanHuo
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 精选
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class RecommendViewModel : ViewModel() {

    val fetchCategoriesResult = StatefulLiveData<MutableList<Category>>()
    val fetchGanHuosResult = StatefulLiveData<MutableList<GanHuo>>()

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

    /**
     * 拉取干货数据
     */
    fun fetchGanHuos(type: String, page: Int, count: Int) {
        viewModelScope.launch {
            try {
                val ganHuos = gankService.getGanHuos(type, page, count)
                fetchGanHuosResult.setValue(ganHuos)
            } catch (e: Exception) {
                fetchGanHuosResult.setException(e)
            }
        }
    }
}