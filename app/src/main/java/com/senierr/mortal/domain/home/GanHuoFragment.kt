package com.senierr.mortal.domain.home

import android.content.Context
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.ui.recyclerview.LinearItemDecoration
import com.senierr.base.support.utils.LogUtil
import com.senierr.base.support.utils.ScreenUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.common.WebViewActivity
import com.senierr.mortal.domain.common.wrapper.LoadMoreWrapper
import com.senierr.mortal.domain.home.vm.GanHuoViewModel
import com.senierr.mortal.domain.home.wrapper.GanHuoWrapper
import com.senierr.mortal.ext.showContentView
import com.senierr.mortal.ext.showEmptyView
import com.senierr.mortal.ext.showLoadingView
import com.senierr.mortal.ext.showNetworkErrorView
import com.senierr.repository.entity.dto.gank.GanHuo
import kotlinx.android.synthetic.main.fragment_home_ganhuo.*

/**
 * 干货列表页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class GanHuoFragment : BaseFragment(R.layout.fragment_home_ganhuo) {

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
    private val ganHuoWrapper = GanHuoWrapper()
    private val loadMoreWrapper = LoadMoreWrapper()

    private lateinit var ganHuoViewModel: GanHuoViewModel

    private var page = 1
    private val pageSize = 10

    override fun onLazyCreate(context: Context) {
        initParam()
        initView(context)
        initViewModel()
        msv_state?.showLoadingView()
        doRefresh()
    }

    private fun initParam() {
        val bundle = arguments
        type = bundle?.getString(TYPE)?: ""
    }

    private fun initView(context: Context) {
        srl_refresh?.setOnRefreshListener { doRefresh() }
        rv_list?.layoutManager = LinearLayoutManager(context)
        rv_list?.addItemDecoration(LinearItemDecoration(dividerSize = ScreenUtil.dp2px(context, 4F)))
        // 列表
        ganHuoWrapper.setOnItemClickListener { _, _, item ->
            WebViewActivity.start(context, item.url, item.title)
        }
        multiTypeAdapter.register(ganHuoWrapper)
        // 加载更多
        loadMoreWrapper.onLoadMoreListener = { doLoadMore() }
        multiTypeAdapter.register(loadMoreWrapper)
        rv_list?.adapter = multiTypeAdapter
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
        srl_refresh?.isRefreshing = false
        if (ganHuos.isEmpty()) {
            msv_state?.showEmptyView()
        } else {
            msv_state?.showContentView()
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