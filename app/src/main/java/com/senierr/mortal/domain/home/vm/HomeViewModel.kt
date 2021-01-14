package com.senierr.mortal.domain.home.vm

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
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class HomeViewModel : ViewModel() {

    private val _categories = MutableSharedFlow<StatefulData<MutableList<Category>>>()
    val categories: SharedFlow<StatefulData<MutableList<Category>>> = _categories

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取分类
     */
    fun fetchCategories() {
        viewModelScope.launch {
            try {
                val result = gankService.getGanHuoCategories()
                _categories.emitSuccess(result)
            } catch (e: Exception) {
                _categories.emitFailure(e)
            }
        }
    }
}