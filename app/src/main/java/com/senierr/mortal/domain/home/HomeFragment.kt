package com.senierr.mortal.domain.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.senierr.base.support.ui.BaseFragment
import com.senierr.mortal.R
import com.senierr.mortal.domain.home.vm.HomeViewModel
import com.senierr.repository.entity.dto.gank.Category
import kotlinx.android.synthetic.main.fragment_home.*


/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private lateinit var homeViewModel: HomeViewModel

    override fun onLazyCreate() {
        initView()
        initViewModel()

        homeViewModel.fetchCategories()
    }

    private fun initView() {

    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        homeViewModel.fetchCategoriesResult.observe(this, { categories ->
            vp_page?.adapter = PageAdapter(this, categories)
            TabLayoutMediator(tl_tab, vp_page) { tab, position ->
                tab.text = categories[position].title
            }.attach()
        }, {

        })
    }

    private inner class PageAdapter(
        fa: Fragment,
        private val categories: MutableList<Category>
    ) : FragmentStateAdapter(fa) {

        override fun getItemCount(): Int = categories.size

        override fun createFragment(position: Int): Fragment {
            val ganHuoFragment = GanHuoFragment()
            val bundle = Bundle()
            bundle.putString("type", categories[position].type)
            ganHuoFragment.arguments = bundle
            return ganHuoFragment
        }
    }
}