package com.senierr.mortal.domain.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.lifecycle.ViewModelProvider
import com.bm.library.PhotoView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.base.support.ext.click
import com.senierr.base.support.ext.setGone
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.ToastUtil
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityImagePreviewBinding
import com.senierr.mortal.domain.common.vm.DownloadViewModel
import com.senierr.mortal.ext.show
import kotlinx.android.parcel.Parcelize
import java.io.File

/**
 * 图片预览页面
 *
 * @author zhouchunjie
 * @date 2019/5/30 10:03
 */
class ImagePreviewActivity : BaseActivity() {

    @Parcelize
    data class ImageItem(
        @DrawableRes
        val resId: Int? = null,
        val url: String? = null,
        val file: File? = null
    ) : Parcelable

    companion object {
        private const val KEY_IMAGES = "images"

        fun start(context: Context, imageItems: MutableList<ImageItem>) {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            intent.putParcelableArrayListExtra(KEY_IMAGES, arrayListOf(*imageItems.toTypedArray()))
            context.startActivity(intent)
        }
    }

    private lateinit var binding: ActivityImagePreviewBinding
    private lateinit var downloadViewModel: DownloadViewModel

    private val imageItems = mutableListOf<ImageItem>()
    private val imagePreviewAdapter = object : androidx.viewpager.widget.PagerAdapter() {
        override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1

        override fun getCount(): Int = imageItems.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val holder = ViewHolder.create(container, R.layout.item_image_preview)
            val pvPreview = holder.findView<PhotoView>(R.id.pv_preview)

            pvPreview?.click { finish() }
            pvPreview?.enable()
            // 索引
            binding.tvIndex.text = getString(R.string.format_index_normal, position+1, imageItems.size)
            // 保存
            binding.btnSave.setGone(true)
            // 图片显示
            val resId = imageItems[position].resId
            val url = imageItems[position].url
            val file = imageItems[position].file
            if (resId != null) {
                pvPreview?.show(resId)
            }else if (url != null) {
                pvPreview?.show(url)
                binding.btnSave.setGone(false)
                binding.btnSave.click {
                    ToastUtil.showShort(this@ImagePreviewActivity, R.string.saving)
                    downloadViewModel.download(url, url, "${System.currentTimeMillis()}.jpg")
                }
            } else if (file != null) {
                pvPreview?.show(file)
            }

            container.addView(holder.itemView)
            return holder.itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
            container.removeView(view as View)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityImagePreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initParams()
        initView()
        initViewModel()
    }

    private fun initParams() {
        val temp = intent.getParcelableArrayListExtra<ImageItem>(KEY_IMAGES)
        imageItems.addAll(temp)
    }

    private fun initView() {
        binding.vpPreview.adapter = imagePreviewAdapter
    }

    private fun initViewModel() {
        downloadViewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        downloadViewModel.downloadResult.observe(this, {
            ToastUtil.showLong(this, getString(R.string.format_download_success, it.absolutePath))
        }, {
            ToastUtil.showLong(this, getString(R.string.format_download_success, it.message))
        })
    }
}