package com.senierr.mortal.domain.home

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.FragmentHomeBinding
import com.senierr.mortal.domain.home.vm.HomeViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.repository.entity.gank.Category

/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel by getViewModel<HomeViewModel>()

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onLazyCreate(context: Context) {
        initViewModel()

        homeViewModel.fetchCategories()
    }

    private fun initViewModel() {
        homeViewModel.fetchCategoriesResult.observe(this, {
            initViewPager(it)
        }, {
            context?.let { ToastUtil.showShort(it, R.string.network_error) }
        })
    }

    private fun initViewPager(categories: MutableList<Category>) {
        val vpPage = binding?.vpPage
        val tlTab = binding?.tlTab
        if (vpPage == null || tlTab == null) return

        vpPage.offscreenPageLimit = 2
        vpPage.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = categories.size

            override fun createFragment(position: Int): Fragment
                    = GanHuoFragment.newInstance(categories[position].type)
        }
        TabLayoutMediator(tlTab, vpPage) { tab, position ->
            tab.text = categories[position].title
        }.attach()
    }
}