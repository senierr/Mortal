package com.senierr.mortal.domain.home.wrapper

import android.widget.ImageView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.mortal.R
import com.senierr.mortal.ext.show
import com.senierr.repository.entity.dto.gank.Girl

/**
 * 首页精选推荐适配器
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class RecommendWrapper : ViewHolderWrapper<Girl>(R.layout.item_home_recommend) {

    override fun onBindViewHolder(holder: ViewHolder, item: Girl) {
        val ivImage = holder.findView<ImageView>(R.id.iv_image)
        ivImage?.show(item.images.firstOrNull())
    }
}