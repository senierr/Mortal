package com.senierr.mortal.domain.home.wrapper

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nex3z.flowlayout.FlowLayout
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.base.support.utils.ScreenUtil
import com.senierr.mortal.R
import com.senierr.mortal.ext.show
import com.senierr.mortal.utils.DateFormatUtil
import com.senierr.repository.entity.dto.gank.GanHuo

/**
 * 首页热门干货适配器
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class GanHuoWrapper : ViewHolderWrapper<GanHuo>(R.layout.item_home_hot) {

    override fun onBindViewHolder(holder: ViewHolder, item: GanHuo) {
        val context = holder.itemView.context

        val ivImage = holder.findView<ImageView>(R.id.iv_image)
        val tvTitle = holder.findView<TextView>(R.id.tv_title)
        val tvDesc = holder.findView<TextView>(R.id.tv_desc)
        val flTag = holder.findView<FlowLayout>(R.id.fl_tag)
        val tvCreator = holder.findView<TextView>(R.id.tv_creator)
        val tvPublishAt = holder.findView<TextView>(R.id.tv_publish_at)

        ivImage?.show(item.images.firstOrNull())
        tvTitle?.text = item.title
        tvDesc?.text = item.desc
        flTag?.removeAllViews()
        flTag?.addView(createAccentTagView(context, item.category))
        flTag?.addView(createAccentTagView(context, item.type))
        tvCreator?.text = item.author
        tvPublishAt?.text = DateFormatUtil.getFormatTime(context, item.publishedAt)
    }

    override fun getSpanSize(item: GanHuo): Int = Integer.MAX_VALUE

    /**
     * 创建普通标签
     */
    private fun createAccentTagView(context: Context, text: String): TextView {
        val tagView = TextView(context)
        tagView.layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        tagView.text = text
        tagView.textSize = 12F
        tagView.setTextColor(ContextCompat.getColor(context, R.color.app_accent))
        tagView.setBackgroundResource(R.drawable.shape_tag_accent)
        tagView.setPadding(
            ScreenUtil.dp2px(context, 4F),
            ScreenUtil.dp2px(context, 2F),
            ScreenUtil.dp2px(context, 4F),
            ScreenUtil.dp2px(context, 2F)
        )
        return tagView
    }
}