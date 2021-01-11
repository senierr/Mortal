package com.senierr.mortal.domain.home.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senierr.mortal.domain.common.vm.StatefulLiveData
import com.senierr.repository.Repository
import com.senierr.repository.entity.bmob.ViewHistory
import com.senierr.repository.entity.gank.GanHuo
import com.senierr.repository.service.api.IArticleService
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.launch

/**
 * 干货
 *
 * @author zhouchunjie
 * @date 2019/7/9
 */
class GanHuoViewModel : ViewModel() {

    val fetchGanHuosResult = StatefulLiveData<MutableList<GanHuo>>()
    val sendViewHistoryResult = StatefulLiveData<ViewHistory>()
    val viewHistories = StatefulLiveData<MutableList<ViewHistory>>()
    val deleteViewHistoryResult = StatefulLiveData<Boolean>()

    private val gankService = Repository.getService<IGankService>()
    private val articleService = Repository.getService<IArticleService>()

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

    /**
     * 发送浏览记录
     */
    fun sendViewHistory(userId: String, articleId: String, articleTitle: String, articleUrl: String) {
        viewModelScope.launch {
            try {
                val viewHistory = articleService.sendViewHistory(userId, articleId, articleTitle, articleUrl)
                sendViewHistoryResult.setValue(viewHistory)
            } catch (e: Exception) {
                sendViewHistoryResult.setException(e)
            }
        }
    }

    /**
     * 获取浏览记录
     *
     * @param userId 用户ID
     * @param page 页数 > 0
     * @param count 每页数据数量 > 0
     */
    fun getViewHistories(userId: String, page: Int, count: Int) {
        viewModelScope.launch {
            try {
                val result = articleService.getViewHistories(userId, page, count)
                viewHistories.setValue(result)
            } catch (e: Exception) {
                viewHistories.setException(e)
            }
        }
    }

    /**
     * 删除浏览记录
     */
    fun deleteViewHistory(objectId: String) {
        viewModelScope.launch {
            try {
                val result = articleService.deleteViewHistory(objectId)
                deleteViewHistoryResult.setValue(result)
            } catch (e: Exception) {
                deleteViewHistoryResult.setException(e)
            }
        }
    }
}