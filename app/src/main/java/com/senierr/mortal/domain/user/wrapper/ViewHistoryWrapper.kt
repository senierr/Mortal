package com.senierr.mortal.domain.user.wrapper

import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.mortal.R
import com.senierr.mortal.utils.DateFormatUtil
import com.senierr.repository.entity.bmob.ViewHistory

/**
 * 浏览记录列表项
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class ViewHistoryWrapper : ViewHolderWrapper<ViewHistory>(R.layout.item_view_history) {

    override fun onBindViewHolder(holder: ViewHolder, item: ViewHistory) {
        val context = holder.itemView.context

        val tvTitle = holder.findView<TextView>(R.id.tv_title)
        val tvPublishAt = holder.findView<TextView>(R.id.tv_publish_at)

        tvTitle?.text = item.articleTitle
        tvPublishAt?.text = DateFormatUtil.getFormatTime(context, item.updatedAt)
    }
}