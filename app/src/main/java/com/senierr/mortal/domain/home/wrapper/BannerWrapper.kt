package com.senierr.mortal.domain.home.wrapper

import android.view.ViewGroup
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.senierr.adapter.internal.ViewHolder
import com.senierr.adapter.internal.ViewHolderWrapper
import com.senierr.mortal.R
import com.senierr.mortal.domain.common.WebViewActivity
import com.senierr.mortal.ext.show
import com.senierr.repository.entity.dto.gank.Banner
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.indicator.CircleIndicator

/**
 * 首页轮播适配器
 *
 * @author zhouchunjie
 * @date 2020/5/10
 */
class BannerWrapper : ViewHolderWrapper<BannerWrapper.HomeBanner>(R.layout.item_home_banner) {

    data class HomeBanner(val banners: MutableList<Banner>)

    override fun onBindViewHolder(holder: ViewHolder, item: HomeBanner) {
        val banner: com.youth.banner.Banner<Banner, HomeBannerAdapter>? = holder.findView(R.id.b_banner)
        banner?.indicator = CircleIndicator(holder.itemView.context)
        banner?.adapter = HomeBannerAdapter(item.banners)
        banner?.setOnBannerListener { data, _ ->
            banner.context?.let {
                WebViewActivity.start(it, (data as Banner).url, data.title)
            }
        }
        banner?.start()
    }

    override fun getSpanSize(item: HomeBanner): Int = Integer.MAX_VALUE

    inner class HomeBannerAdapter(banners: MutableList<Banner>) : BannerAdapter<Banner, ViewHolder>(banners) {
        override fun onCreateHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
            val imageView = ImageView(parent!!.context)
            imageView.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            imageView.scaleType = ImageView.ScaleType.CENTER_CROP
            return ViewHolder.Companion.create(imageView)
        }

        override fun onBindView(holder: ViewHolder?, data: Banner?, position: Int, size: Int) {
            val imageView = holder?.itemView
            if (imageView != null && imageView is ImageView && data != null) {
                imageView.show(data.image)
            }
        }
    }
}