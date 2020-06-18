package com.senierr.mortal.domain.recommend

import androidx.lifecycle.ViewModelProvider
import com.senierr.base.support.ui.BaseFragment
import com.senierr.mortal.R
import com.senierr.mortal.domain.recommend.vm.RecommendViewModel

/**
 * 公众号页面
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class RecommendFragment : BaseFragment(R.layout.fragment_recommend) {

    private lateinit var recommendViewModel: RecommendViewModel

    override fun onLazyCreate() {
        initView()
        initViewModel()

//        recommendViewModel.fetchCategories()
    }

    private fun initView() {

    }

    private fun initViewModel() {
        recommendViewModel = ViewModelProvider(this).get(RecommendViewModel::class.java)

    }
}