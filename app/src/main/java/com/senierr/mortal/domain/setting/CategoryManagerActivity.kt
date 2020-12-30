package com.senierr.mortal.domain.setting

import android.os.Bundle
import android.view.LayoutInflater
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.databinding.ActivityCategoryManagerBinding

/**
 * 标签管理页面
 *
 * @author zhouchunjie
 * @date 2019/7/6
 */
class CategoryManagerActivity : BaseActivity<ActivityCategoryManagerBinding>() {

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityCategoryManagerBinding {
        return ActivityCategoryManagerBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initView()
    }

    private fun initView() {

    }
}
