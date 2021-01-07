package com.senierr.mortal.domain.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.senierr.base.support.ui.BaseFragment
import com.senierr.mortal.R
import com.senierr.mortal.databinding.FragmentHomeBinding
import com.senierr.mortal.domain.category.CategoryManagerActivity
import com.senierr.mortal.domain.home.vm.HomeViewModel
import com.senierr.mortal.ext.getViewModel
import com.senierr.mortal.ext.showToast
import com.senierr.repository.entity.gank.Category

/**
 * 首页
 *
 * @author zhouchunjie
 * @date 2019/7/8 21:21
 */
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel by getViewModel<HomeViewModel>()

    // 当前加载的分类标签
    private val currentCategories = mutableListOf<Category>()

    override fun createViewBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_fragment_home, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_category -> {
                startActivity(Intent(context, CategoryManagerActivity::class.java))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onStart() {
        super.onStart()
        homeViewModel.fetchCategories()
    }

    private fun initView() {
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).setSupportActionBar(binding?.tbTop)

        val vpPage = binding?.vpPage
        val tlTab = binding?.tlTab
        if (vpPage != null && tlTab != null) {
            vpPage.adapter = object : FragmentStateAdapter(this) {

                val itemIds = mutableListOf<Long>()

                override fun getItemCount(): Int = currentCategories.size

                override fun createFragment(position: Int): Fragment
                        = GanHuoFragment.newInstance(currentCategories[position].type)

                override fun getItemId(position: Int): Long {
                    return currentCategories[position].hashCode().toLong()
                }

                override fun containsItem(itemId: Long): Boolean {
                    return itemIds.contains(itemId)
                }
            }
            TabLayoutMediator(tlTab, vpPage) { tab, position ->
                tab.text = currentCategories[position].title
            }.attach()
        }
    }

    private fun initViewModel() {
        homeViewModel.fetchCategoriesResult.observe(this, {
            // 判断是否数据变动
            if (isChanged(it)) {
                currentCategories.clear()
                currentCategories.addAll(it)
                binding?.vpPage?.adapter?.notifyDataSetChanged()
            }
        }, {
            context?.showToast(R.string.network_error)
        })
    }

    /**
     * 判断数据是否变动
     */
    private fun isChanged(categories: MutableList<Category>): Boolean {
        if (currentCategories.size != categories.size) return true
        currentCategories.forEachIndexed { index, category ->
            if (categories[index].type != category.type) {
                return true
            }
        }
        return false
    }
}