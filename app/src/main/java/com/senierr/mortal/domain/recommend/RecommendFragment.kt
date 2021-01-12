package com.senierr.mortal.domain.recommend

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.base.support.ext.getViewModel
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.ui.recyclerview.GridItemDecoration
import com.senierr.base.support.utils.ScreenUtil
import com.senierr.mortal.databinding.FragmentRecommendBinding
import com.senierr.mortal.domain.common.ImagePreviewActivity
import com.senierr.mortal.domain.common.wrapper.LoadMoreWrapper
import com.senierr.mortal.domain.recommend.vm.RecommendViewModel
import com.senierr.mortal.domain.recommend.wrapper.RecommendWrapper
import com.senierr.mortal.ext.*
import com.senierr.repository.entity.gank.Girl

/**
 * 精选页面
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class RecommendFragment : BaseFragment<FragmentRecommendBinding>() {

    private val multiTypeAdapter = MultiTypeAdapter()
    private val recommendWrapper = RecommendWrapper()
    private val loadMoreWrapper = LoadMoreWrapper()

    private val recommendViewModel by getViewModel<RecommendViewModel>()

    private var page = 1
    private val pageSize = 10

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentRecommendBinding {
        return FragmentRecommendBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let {
            initView(it)
            initViewModel()
            binding?.msvState?.showLoadingView()
            doRefresh()
        }
    }

    private fun initView(context: Context) {
        binding?.srlRefresh?.setOnRefreshListener { doRefresh() }
        binding?.rvList?.layoutManager = GridLayoutManager(context, 2)
        binding?.rvList?.addItemDecoration(GridItemDecoration(ScreenUtil.dp2px(context, 2F), true))
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
        binding?.rvList?.adapter = multiTypeAdapter
    }

    private fun initViewModel() {
        recommendViewModel.fetchGirlsResult.observe(this, {
            if (page == 1) {
                renderRefresh(it)
            } else {
                renderLoadMore(it)
            }
        }, {
            if (page == 1) {
                binding?.msvState?.showNetworkErrorView {
                    binding?.msvState?.showLoadingView()
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
        binding?.srlRefresh?.isRefreshing = false
        if (girls.isEmpty()) {
            binding?.msvState?.showEmptyView()
        } else {
            binding?.msvState?.showContentView()
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