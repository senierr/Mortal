package com.senierr.mortal.domain.recommend.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.Girl
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 精选
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class RecommendViewModel : ViewModel() {

    val fetchGirlsResult = StatefulLiveData<MutableList<Girl>>()

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取妹纸数据
     */
    fun fetchGirls(page: Int, count: Int) {
        viewModelScope.launch {
            try {
                val ganHuos = gankService.getGirls(page, count)
                fetchGirlsResult.setValue(ganHuos)
            } catch (e: Exception) {
                fetchGirlsResult.setException(e)
            }
        }
    }
}