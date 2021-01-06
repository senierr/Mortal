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
class SettingViewModel : ViewModel() {

    val ganHuoCategories = StatefulLiveData<MutableList<Category>>()

    private val gankService = Repository.getService<IGankService>()

    /**
     * 获取版本号
     */
    fun getVersionName() {
        viewModelScope.launch {
            try {
                val categories = gankService.getGanHuoCategories()
                ganHuoCategories.setValue(categories)
            } catch (e: Exception) {
                ganHuoCategories.setException(e)
            }
        }
    }
}