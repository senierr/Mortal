package com.senierr.mortal.domain.recommend.wrapper

import android.widget.ImageView
import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.mortal.R
import com.senierr.mortal.ext.show
import com.senierr.repository.entity.gank.Girl

/**
 * 精选推荐适配器
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class RecommendWrapper : ViewHolderWrapper<Girl>(R.layout.item_recommend) {

    override fun onBindViewHolder(holder: ViewHolder, item: Girl) {
        val ivImage = holder.findView<ImageView>(R.id.iv_image)
        val tvTitle = holder.findView<TextView>(R.id.tv_title)
        val tvView = holder.findView<TextView>(R.id.tv_view)

        ivImage?.show(item.images.firstOrNull()?: "")
        tvTitle?.text = item.title
        tvView?.text = item.views.toString()
    }
}