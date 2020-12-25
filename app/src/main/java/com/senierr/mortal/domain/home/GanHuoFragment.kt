package com.senierr.mortal.domain.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.ui.recyclerview.LinearItemDecoration
import com.senierr.base.support.utils.ScreenUtil
import com.senierr.mortal.databinding.FragmentHomeGanhuoBinding
import com.senierr.mortal.domain.common.WebViewActivity
import com.senierr.mortal.domain.common.wrapper.LoadMoreWrapper
import com.senierr.mortal.domain.home.vm.GanHuoViewModel
import com.senierr.mortal.domain.home.wrapper.GanHuoMoreImageWrapper
import com.senierr.mortal.domain.home.wrapper.GanHuoNoImageWrapper
import com.senierr.mortal.domain.home.wrapper.GanHuoOneImageWrapper
import com.senierr.mortal.ext.showContentView
import com.senierr.mortal.ext.showEmptyView
import com.senierr.mortal.ext.showLoadingView
import com.senierr.mortal.ext.showNetworkErrorView
import com.senierr.repository.entity.gank.GanHuo
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 干货列表页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class GanHuoFragment : BaseFragment() {

    companion object {
        private const val TYPE = "type"

        fun newInstance(type: String): GanHuoFragment {
            val ganHuoFragment = GanHuoFragment()
            val bundle = Bundle()
            bundle.putString(TYPE, type)
            ganHuoFragment.arguments = bundle
            return ganHuoFragment
        }
    }

    private lateinit var type: String

    private val multiTypeAdapter = MultiTypeAdapter()
    private val moreImageWrapper = GanHuoMoreImageWrapper()
    private val oneImageWrapper = GanHuoOneImageWrapper()
    private val noImageWrapper = GanHuoNoImageWrapper()
    private val loadMoreWrapper = LoadMoreWrapper()

    private var binding: FragmentHomeGanhuoBinding? = null
    private lateinit var ganHuoViewModel: GanHuoViewModel

    private var page = 1
    private val pageSize = 10

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeGanhuoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onLazyCreate(context: Context) {
        initParam()
        initView(context)
        initViewModel()
        lifecycleScope.launch {
            binding?.msvState?.showLoadingView()
            delay(200)
            doRefresh()
        }
    }

    private fun initParam() {
        val bundle = arguments
        type = bundle?.getString(TYPE)?: ""
    }

    private fun initView(context: Context) {
        binding?.srlRefresh?.setOnRefreshListener { doRefresh() }
        binding?.rvList?.layoutManager = LinearLayoutManager(context)
        binding?.rvList?.addItemDecoration(LinearItemDecoration(dividerSize = ScreenUtil.dp2px(context, 4F)))
        // 列表
        moreImageWrapper.setOnItemClickListener { _, _, item ->
            WebViewActivity.start(context, item.url, item.title)
        }
        oneImageWrapper.setOnItemClickListener { _, _, item ->
            WebViewActivity.start(context, item.url, item.title)
        }
        noImageWrapper.setOnItemClickListener { _, _, item ->
            WebViewActivity.start(context, item.url, item.title)
        }
        multiTypeAdapter.register(moreImageWrapper, oneImageWrapper, noImageWrapper) { item ->
            return@register when (item.images.size) {
                0 -> GanHuoNoImageWrapper::class.java
                1 -> GanHuoOneImageWrapper::class.java
                else -> GanHuoMoreImageWrapper::class.java
            }
        }
        // 加载更多
        loadMoreWrapper.onLoadMoreListener = { doLoadMore() }
        multiTypeAdapter.register(loadMoreWrapper)
        binding?.rvList?.adapter = multiTypeAdapter
    }

    private fun initViewModel() {
        ganHuoViewModel = ViewModelProvider(this).get(GanHuoViewModel::class.java)

        ganHuoViewModel.fetchGanHuosResult.observe(this, {
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
        ganHuoViewModel.fetchGanHuos(type, page, pageSize)
    }

    /**
     * 加载更多
     */
    private fun doLoadMore() {
        ganHuoViewModel.fetchGanHuos(type, page, pageSize)
    }

    /**
     * 渲染刷新
     */
    private fun renderRefresh(ganHuos: MutableList<GanHuo>) {
        binding?.srlRefresh?.isRefreshing = false
        if (ganHuos.isEmpty()) {
            binding?.msvState?.showEmptyView()
        } else {
            binding?.msvState?.showContentView()
            multiTypeAdapter.data.clear()
            multiTypeAdapter.data.addAll(ganHuos)
            multiTypeAdapter.data.add(loadMoreWrapper.loadMoreBean)
            multiTypeAdapter.notifyDataSetChanged()
            page++
        }
    }

    /**
     * 渲染加载更多
     */
    private fun renderLoadMore(ganHuos: MutableList<GanHuo>) {
        if (ganHuos.isEmpty()) {
            loadMoreWrapper.loadNoMore()
        } else {
            val startPosition = multiTypeAdapter.data.size - 1
            multiTypeAdapter.data.addAll(startPosition, ganHuos)
            multiTypeAdapter.notifyItemRangeInserted(startPosition, ganHuos.size)
            loadMoreWrapper.loadCompleted()
            page++
        }
    }
}