package com.senierr.mortal.domain.category.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.Category
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * 分类标签
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class CategoryViewModel : ViewModel() {

    private val _ganHuoCategories = MutableSharedFlow<StatefulData<MutableList<Category>>>()
    val ganHuoCategories: SharedFlow<StatefulData<MutableList<Category>>> = _ganHuoCategories

    private val _saveCategoryResult = MutableSharedFlow<StatefulData<Boolean>>()
    val saveCategoryResult: SharedFlow<StatefulData<Boolean>> = _saveCategoryResult

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取分类标签
     */
    fun fetchGanHuoCategories() {
        viewModelScope.launch {
            try {
                val categories = gankService.getGanHuoCategories()
                _ganHuoCategories.emitSuccess(categories)
            } catch (e: Exception) {
                _ganHuoCategories.emitFailure(e)
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
                _saveCategoryResult.emitSuccess(result)
            } catch (e: Exception) {
                _saveCategoryResult.emitFailure(e)
            }
        }
    }
}