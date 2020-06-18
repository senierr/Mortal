package com.senierr.mortal.domain.home.wrapper

import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.mortal.R

/**
 * 首页分类适配器
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class CategoryWrapper : ViewHolderWrapper<String>(R.layout.item_home_category) {

    override fun onBindViewHolder(holder: ViewHolder, item: String) {
        val tvCategory = holder.findView<TextView>(R.id.tv_category)
        tvCategory?.text = item
    }

    override fun getSpanSize(item: String): Int = Integer.MAX_VALUE
}