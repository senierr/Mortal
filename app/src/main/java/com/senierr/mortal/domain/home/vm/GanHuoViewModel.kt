package com.senierr.mortal.domain.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.GanHuo
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * 干货
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class GanHuoViewModel : ViewModel() {

    private val _ganHuos = MutableSharedFlow<StatefulData<MutableList<GanHuo>>>()
    val ganHuos: SharedFlow<StatefulData<MutableList<GanHuo>>> = _ganHuos

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取干货数据
     */
    fun fetchGanHuos(type: String, page: Int, count: Int) {
        viewModelScope.launch {
            try {
                val ganHuos = gankService.getGanHuos(type, page, count)
                _ganHuos.emitSuccess(ganHuos)
            } catch (e: Exception) {
                _ganHuos.emitFailure(e)
            }
        }
    }
}