package com.senierr.mortal.domain.recommend

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.ui.recyclerview.GridItemDecoration
import com.senierr.base.support.utils.ScreenUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.common.ImagePreviewActivity
import com.senierr.mortal.domain.common.wrapper.LoadMoreWrapper
import com.senierr.mortal.domain.recommend.vm.RecommendViewModel
import com.senierr.mortal.domain.recommend.wrapper.RecommendWrapper
import com.senierr.mortal.ext.showContentView
import com.senierr.mortal.ext.showEmptyView
import com.senierr.mortal.ext.showLoadingView
import com.senierr.mortal.ext.showNetworkErrorView
import com.senierr.repository.entity.gank.Girl
import kotlinx.android.synthetic.main.fragment_home_ganhuo.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 精选页面
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
@ExperimentalCoroutinesApi
class RecommendFragment : BaseFragment(R.layout.fragment_recommend) {

    private val multiTypeAdapter = MultiTypeAdapter()
    private val recommendWrapper = RecommendWrapper()
    private val loadMoreWrapper = LoadMoreWrapper()

    private lateinit var recommendViewModel: RecommendViewModel

    private var page = 1
    private val pageSize = 10

    override fun onLazyCreate(context: Context) {
        initView(context)
        initViewModel()
        msv_state?.showLoadingView()
        doRefresh()
    }

    private fun initView(context: Context) {
        srl_refresh?.setOnRefreshListener { doRefresh() }
        rv_list?.layoutManager = GridLayoutManager(context, 2)
        rv_list?.addItemDecoration(GridItemDecoration(ScreenUtil.dp2px(context, 2F), true))
        // 列表
        recommendWrapper.setOnItemClickListener { _, _, item ->
            val imageItems = mutableListOf<ImagePreviewActivity.ImageItem>()
            item.images.forEach { url ->
                imageItems.add(ImagePreviewActivity.ImageItem(url = url))
            }
            ImagePreviewActivity.start(context, imageItems)
        }
        multiTypeAdapter.register(recommendWrapper)
        // 加载更多
        loadMoreWrapper.onLoadMoreListener = { doLoadMore() }
        multiTypeAdapter.register(loadMoreWrapper)
        rv_list?.adapter = multiTypeAdapter
    }

    private fun initViewModel() {
        recommendViewModel = ViewModelProvider(this).get(RecommendViewModel::class.java)

        recommendViewModel.fetchGirlsResult.observe(this, {
            if (page == 1) {
                renderRefresh(it)
            } else {
                renderLoadMore(it)
            }
        }, {
            if (page == 1) {
                msv_state?.showNetworkErrorView {
                    msv_state?.showLoadingView()
                    doRefresh()
                }
            } else {
                loadMoreWrapper.loadFailure()
            }
        })
    }

    /**
     * 刷新
     */
    private fun doRefresh() {
        page = 1
        recommendViewModel.fetchGirls(page, pageSize)
    }

    /**
     * 加载更多
     */
    private fun doLoadMore() {
        recommendViewModel.fetchGirls(page, pageSize)
    }

    /**
     * 渲染刷新
     */
    private fun renderRefresh(girls: MutableList<Girl>) {
        srl_refresh?.isRefreshing = false
        if (girls.isEmpty()) {
            msv_state?.showEmptyView()
        } else {
            msv_state?.showContentView()
            multiTypeAdapter.data.clear()
            multiTypeAdapter.data.addAll(girls)
            multiTypeAdapter.data.add(loadMoreWrapper.loadMoreBean)
            multiTypeAdapter.notifyDataSetChanged()
            page++
        }
    }

    /**
     * 渲染加载更多
     */
    private fun renderLoadMore(girls: MutableList<Girl>) {
        if (girls.isEmpty()) {
            loadMoreWrapper.loadNoMore()
        } else {
            val startPosition = multiTypeAdapter.data.size - 1
            multiTypeAdapter.data.addAll(startPosition, girls)
            multiTypeAdapter.notifyItemRangeInserted(startPosition, girls.size)
            loadMoreWrapper.loadCompleted()
            page++
        }
    }
}