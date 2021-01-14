package com.senierr.mortal.domain.recommend.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.base.support.arch.StatefulData
import com.senierr.base.support.arch.ext.emitFailure
import com.senierr.base.support.arch.ext.emitSuccess
import com.senierr.repository.Repository
import com.senierr.repository.entity.gank.Girl
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch

/**
 * 精选
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class RecommendViewModel : ViewModel() {

    private val _girls = MutableSharedFlow<StatefulData<MutableList<Girl>>>()
    val girls: SharedFlow<StatefulData<MutableList<Girl>>> = _girls

    private val gankService = Repository.getService<IGankService>()

    /**
     * 拉取妹纸数据
     */
    fun fetchGirls(page: Int, count: Int) {
        viewModelScope.launch {
            try {
                val girls = gankService.getGirls(page, count)
                _girls.emitSuccess(girls)
            } catch (e: Exception) {
                _girls.emitFailure(e)
            }
        }
    }
}