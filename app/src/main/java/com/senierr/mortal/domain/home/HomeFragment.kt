package com.senierr.mortal.domain.home

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.home.vm.HomeViewModel
import com.senierr.repository.entity.gank.Category
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.ExperimentalCoroutinesApi

/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
@ExperimentalCoroutinesApi
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private lateinit var homeViewModel: HomeViewModel

    override fun onLazyCreate(context: Context) {
        initViewModel()

        homeViewModel.fetchCategories()
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.fetchCategoriesResult.observe(this, {
            initViewPager(it)
        }, {
            context?.let { ToastUtil.showShort(it, R.string.network_error) }
        })
    }

    private fun initViewPager(categories: MutableList<Category>) {
        vp_page?.offscreenPageLimit = 2
        vp_page?.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount(): Int = categories.size

            override fun createFragment(position: Int): Fragment
                    = GanHuoFragment.newInstance(categories[position].type)
        }
        TabLayoutMediator(tl_tab, vp_page) { tab, position ->
            tab.text = categories[position].title
        }.attach()
    }
}