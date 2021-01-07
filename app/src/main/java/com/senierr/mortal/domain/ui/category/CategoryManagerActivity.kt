package com.senierr.mortal.domain.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.GridLayoutManager
import com.senierr.adapter.internal.MultiTypeAdapter
import com.senierr.mortal.support.ui.BaseActivity
import com.senierr.mortal.support.ui.recyclerview.GridItemDecoration
import com.senierr.mortal.support.utils.ScreenUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityCategoryManagerBinding
import com.senierr.mortal.domain.ui.category.vm.CategoryViewModel
import com.senierr.mortal.domain.ui.category.wrapper.CategoryWrapper
import com.senierr.mortal.repository.entity.gank.Category
import com.senierr.mortal.support.ext.*

/**
 * 标签管理页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class CategoryManagerActivity : BaseActivity<ActivityCategoryManagerBinding>() {

    private val categoryViewModel by getViewModel<CategoryViewModel>()

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
        categoryViewModel.ganHuoCategories.observe(this, {
            if (it.isEmpty()) {
                binding.msvState.showEmptyView()
            } else {
                binding.msvState.showContentView()
                multiTypeAdapter.data.clear()
                multiTypeAdapter.data.addAll(it)
                multiTypeAdapter.notifyDataSetChanged()
            }
        }, {
            binding.msvState.showNetworkErrorView { doRefresh() }
        })
        categoryViewModel.saveCategories.observe(this) {
            finish()
        }
    }

    private fun doRefresh() {
        categoryViewModel.fetchGanHuoCategories()
    }
}
