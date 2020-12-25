package com.senierr.mortal.domain.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.senierr.base.support.ui.BaseFragment
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.FragmentHomeBinding
import com.senierr.mortal.domain.home.vm.HomeViewModel
import com.senierr.repository.entity.gank.Category

/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class HomeFragment : BaseFragment() {

    private var binding: FragmentHomeBinding? = null
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

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