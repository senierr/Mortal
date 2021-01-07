package com.senierr.mortal.domain.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.mortal.repository.Repository
import com.senierr.mortal.repository.entity.gank.GanHuo
import com.senierr.mortal.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 干货
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class GanHuoViewModel : ViewModel() {

    val fetchGanHuosResult = StatefulLiveData<MutableList<GanHuo>>()

    private val gankService = Repository.getService<IGankService>()

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