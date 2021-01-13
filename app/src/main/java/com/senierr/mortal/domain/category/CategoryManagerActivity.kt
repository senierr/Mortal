package com.senierr.mortal.domain.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.base.support.arch.ext.doOnFailure
import com.senierr.base.support.arch.ext.doOnSuccess
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.ui.recyclerview.GridItemDecoration
import com.senierr.base.support.utils.ScreenUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityCategoryManagerBinding
import com.senierr.mortal.domain.category.vm.CategoryViewModel
import com.senierr.mortal.domain.category.wrapper.CategoryWrapper
import com.senierr.mortal.ext.openItemDrag
import com.senierr.mortal.ext.showContentView
import com.senierr.mortal.ext.showEmptyView
import com.senierr.mortal.ext.showNetworkErrorView
import com.senierr.repository.entity.gank.Category
import kotlinx.coroutines.flow.collect

/**
 * 标签管理页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class CategoryManagerActivity : BaseActivity<ActivityCategoryManagerBinding>() {

    private val categoryViewModel: CategoryViewModel by viewModel()

    private val multiTypeAdapter = MultiTypeAdapter()
    private val categoryWrapper = CategoryWrapper()

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityCategoryManagerBinding {
        return ActivityCategoryManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
        initViewModel()
        doRefresh()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_common_done, menu)
        return super.onCreateOptionsMenu(menu)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.tab_done -> {
                categoryViewModel.saveGanHuoCategories(multiTypeAdapter.data as MutableList<Category>)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initView() {
        setSupportActionBar(binding.tbTop)
        binding.tbTop.navigationIcon?.setTint(getColor(R.color.btn_black))
        binding.tbTop.setNavigationOnClickListener { finish() }

        binding.rvList.layoutManager = GridLayoutManager(this, 4)
        binding.rvList.addItemDecoration(GridItemDecoration(ScreenUtil.dp2px(this, 8F), true))
        binding.rvList.openItemDrag(multiTypeAdapter, multiTypeAdapter.data)
        multiTypeAdapter.register(categoryWrapper)
        binding.rvList.adapter = multiTypeAdapter
    }

    private fun initViewModel() {
        lifecycleScope.launchWhenStarted {
            categoryViewModel.ganHuoCategories
                .doOnSuccess {
                    if (it.isEmpty()) {
                        binding.msvState.showEmptyView()
                    } else {
                        binding.msvState.showContentView()
                        multiTypeAdapter.data.clear()
                        multiTypeAdapter.data.addAll(it)
                        multiTypeAdapter.notifyDataSetChanged()
                    }
                }
                .doOnFailure {
                    binding.msvState.showNetworkErrorView { doRefresh() }
                }
                .collect()
        }

        lifecycleScope.launchWhenStarted {
            categoryViewModel.saveCategoryResult
                .doOnSuccess { finish() }
                .collect()
        }
    }

    private fun doRefresh() {
        categoryViewModel.fetchGanHuoCategories()
    }
}
