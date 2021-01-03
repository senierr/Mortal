package com.senierr.mortal.domain.home.wrapper

import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.widget.ImageView
import android.widget.TextView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.mortal.R
import com.senierr.mortal.ext.showImage
import com.senierr.mortal.utils.DateFormatUtil
import com.senierr.repository.entity.gank.GanHuo

/**
 * 首页热门干货适配器
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class GanHuoMoreImageWrapper : ViewHolderWrapper<GanHuo>(R.layout.item_home_ganhuo_more_image) {

    override fun onBindViewHolder(holder: ViewHolder, item: GanHuo) {
        val context = holder.itemView.context

        val ivImage1 = holder.findView<ImageView>(R.id.iv_image1)
        val ivImage2 = holder.findView<ImageView>(R.id.iv_image2)
        val ivImage3 = holder.findView<ImageView>(R.id.iv_image3)
        val tvTitle = holder.findView<TextView>(R.id.tv_title)
        val tvDesc = holder.findView<TextView>(R.id.tv_desc)
        val tvCreator = holder.findView<TextView>(R.id.tv_creator)
        val tvPublishAt = holder.findView<TextView>(R.id.tv_publish_at)

        item.images.forEachIndexed { index, s ->
            when (index) {
                0 -> s?.let { ivImage1?.showImage(it) }
                1 -> s?.let { ivImage2?.showImage(it) }
                2 -> s?.let { ivImage3?.showImage(it) }
            }
        }
        tvTitle?.text = item.title
        tvDesc?.text = item.desc
        val authorStr = context.getString(R.string.format_author, item.author)
        val spannableString = SpannableString(authorStr)
        val colorSpan = ForegroundColorSpan(context.getColor(R.color.text_theme))
        spannableString.setSpan(colorSpan, 3, spannableString.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
        tvCreator?.text = spannableString
        tvPublishAt?.text = DateFormatUtil.getFormatTime(context, item.publishedAt)
    }
}