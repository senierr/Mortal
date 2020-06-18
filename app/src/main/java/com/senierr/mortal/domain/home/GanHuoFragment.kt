package com.senierr.mortal.domain.home

import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.home.vm.GanHuoViewModel
import com.senierr.mortal.domain.home.wrapper.GanHuoWrapper
import com.senierr.mortal.ext.showContentView
import com.senierr.mortal.ext.showLoadingView
import kotlinx.android.synthetic.main.fragment_home_ganhuo.*

/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class GanHuoFragment : BaseFragment(R.layout.fragment_home_ganhuo) {

    private lateinit var type: String

    private val multiTypeAdapter = MultiTypeAdapter()
    private val ganHuoWrapper = GanHuoWrapper()

    private lateinit var ganHuoViewModel: GanHuoViewModel

    private var page = 1
    private val pageSize = 10

    override fun onLazyCreate() {
        LogUtil.logE("onLazyCreate")
        initParam()
        initView()
        initViewModel()
        msv_state?.showLoadingView()
        doRefresh()
    }

    private fun initParam() {
        val bundle = arguments
        type = bundle?.getString("type")?: ""
    }

    private fun initView() {
        srl_refresh?.setOnRefreshListener {
            doRefresh()
        }
        rv_list?.layoutManager = LinearLayoutManager(context)
        multiTypeAdapter.register(ganHuoWrapper)
        rv_list?.adapter = multiTypeAdapter
    }

    private fun initViewModel() {
        ganHuoViewModel = ViewModelProvider(this).get(GanHuoViewModel::class.java)

        ganHuoViewModel.fetchGanHuosResult.observe(this, {
            msv_state?.showContentView()
            multiTypeAdapter.data.clear()
            multiTypeAdapter.data.addAll(it)
            multiTypeAdapter.notifyDataSetChanged()
        }, {

        })
    }

    /**
     * 启动
     */
    private fun doRefresh() {
        page = 1
        ganHuoViewModel.fetchGanHuos(type, page, pageSize)
    }
}