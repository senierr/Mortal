package com.senierr.mortal.domain.user

import android.os.Bundle
import android.view.LayoutInflater
import androidx.recyclerview.widget.LinearLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.ui.recyclerview.LinearItemDecoration
import com.senierr.base.support.utils.ScreenUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityViewHistoryBinding
import com.senierr.mortal.domain.common.WebViewActivity
import com.senierr.mortal.domain.common.wrapper.LoadMoreWrapper
import com.senierr.mortal.domain.home.vm.GanHuoViewModel
import com.senierr.mortal.domain.user.vm.UserInfoViewModel
import com.senierr.mortal.domain.user.wrapper.ViewHistoryWrapper
import com.senierr.mortal.ext.*
import com.senierr.repository.entity.bmob.UserInfo
import com.senierr.repository.entity.bmob.ViewHistory

/**
 * 注册页面
 *
 * @author zhouchunjie
 * @date 2021/1/4
 */
class ViewHistoryActivity : BaseActivity<ActivityViewHistoryBinding>() {

    private val multiTypeAdapter = MultiTypeAdapter()
    private val viewHistoryWrapper = ViewHistoryWrapper()
    private val loadMoreWrapper = LoadMoreWrapper()

    private val ganHuoViewModel by viewModel<GanHuoViewModel>()
    private val userInfoViewModel by viewModel<UserInfoViewModel>()

    private var page = 0
    private val pageSize = 10

    private var currentUserInfo: UserInfo? = null

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityViewHistoryBinding {
        return ActivityViewHistoryBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onStart() {
        super.onStart()
        userInfoViewModel.getLoggedCacheUserInfo()
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.rvList.layoutManager = LinearLayoutManager(this)
        binding.rvList.addItemDecoration(LinearItemDecoration(dividerSize = ScreenUtil.dp2px(this, 4F)))
        // 列表
        viewHistoryWrapper.setOnItemClickListener { _, _, item ->
            WebViewActivity.start(this, item.articleUrl, item.articleTitle)
            currentUserInfo?.let {
                // 发送浏览记录
                ganHuoViewModel.sendViewHistory(it.objectId, item.articleId, item.articleTitle, item.articleUrl)
            }
        }
        multiTypeAdapter.register(viewHistoryWrapper)
        // 加载更多
        loadMoreWrapper.onLoadMoreListener = {
            currentUserInfo?.objectId?.let { userId ->
                ganHuoViewModel.getViewHistories(userId, page, pageSize)
            }
        }
        multiTypeAdapter.register(loadMoreWrapper)
        binding.rvList.adapter = multiTypeAdapter
    }

    private fun initViewModel() {
        userInfoViewModel.loggedCacheUserInfo.observe(this, {
            currentUserInfo = it
            currentUserInfo?.objectId?.let { userId ->
                ganHuoViewModel.getViewHistories(userId, page, pageSize)
            }
        })
        ganHuoViewModel.viewHistories.observe(this, {
            if (page == 0) {
                renderRefresh(it)
            } else {
                renderLoadMore(it)
            }
        }, {
            if (page == 0) {
                binding.msvState.showNetworkErrorView {
                    binding.msvState.showLoadingView()
                    currentUserInfo?.objectId?.let { userId ->
                        ganHuoViewModel.getViewHistories(userId, page, pageSize)
                    }
                }
            } else {
                loadMoreWrapper.loadFailure()
            }
        })
    }

    /**
     * 渲染刷新
     */
    private fun renderRefresh(viewHistories: MutableList<ViewHistory>) {
        if (viewHistories.isEmpty()) {
            binding.msvState.showEmptyView()
        } else {
            binding.msvState.showContentView()
            multiTypeAdapter.data.clear()
            multiTypeAdapter.data.addAll(viewHistories)
            multiTypeAdapter.data.add(loadMoreWrapper.loadMoreBean)
            multiTypeAdapter.notifyDataSetChanged()
            page++
        }
    }

    /**
     * 渲染加载更多
     */
    private fun renderLoadMore(viewHistories: MutableList<ViewHistory>) {
        if (viewHistories.isEmpty()) {
            loadMoreWrapper.loadNoMore()
        } else {
            val startPosition = multiTypeAdapter.data.size - 1
            multiTypeAdapter.data.addAll(startPosition, viewHistories)
            multiTypeAdapter.notifyItemRangeInserted(startPosition, viewHistories.size)
            loadMoreWrapper.loadCompleted()
            page++
        }
    }
}
