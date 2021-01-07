package com.senierr.mortal.domain.category.wrapper

import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.mortal.R
import com.senierr.mortal.repository.entity.gank.Category

/**
 * 分类标签管理适配器
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class CategoryWrapper : ViewHolderWrapper<Category>(R.layout.item_setting_category) {

    override fun onBindViewHolder(holder: ViewHolder, item: Category) {
        val tvCategory = holder.findView<TextView>(R.id.tv_category)
        tvCategory?.text = item.title
    }
}