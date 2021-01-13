package com.senierr.mortal.domain.common

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.lifecycle.lifecycleScope
import com.bm.library.PhotoView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.base.support.arch.ext.doOnFailure
import com.senierr.base.support.arch.ext.doOnSuccess
import com.senierr.base.support.arch.ext.viewModel
import com.senierr.base.support.ext.click
import com.senierr.base.support.ext.setGone
import com.senierr.base.support.ui.BaseActivity
import com.senierr.mortal.R
import com.senierr.mortal.databinding.ActivityImagePreviewBinding
import com.senierr.mortal.domain.common.vm.DownloadViewModel
import com.senierr.mortal.ext.showImage
import com.senierr.mortal.ext.showToast
import kotlinx.coroutines.flow.collect
import kotlinx.parcelize.Parcelize
import java.io.File

/**
 * 图片预览页面
 *
 * @author zhouchunjie
 * @date 2019/5/30 10:03
 */
class ImagePreviewActivity : BaseActivity<ActivityImagePreviewBinding>() {

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

    private val downloadViewModel: DownloadViewModel by viewModel()

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
            binding.tvIndex.text =
                getString(R.string.format_index_normal, position + 1, imageItems.size)
            // 保存
            binding.btnSave.setGone(true)
            // 图片显示
            val resId = imageItems[position].resId
            val url = imageItems[position].url
            val file = imageItems[position].file
            if (resId != null) {
                pvPreview?.showImage(resId)
            } else if (url != null) {
                pvPreview?.showImage(url)
                binding.btnSave.setGone(false)
                binding.btnSave.click {
                    showToast(R.string.saving)
                    downloadViewModel.download(url, "${System.currentTimeMillis()}.jpg")
                }
            } else if (file != null) {
                pvPreview?.showImage(file)
            }

            container.addView(holder.itemView)
            return holder.itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
            container.removeView(view as View)
        }
    }

    override fun createViewBinding(layoutInflater: LayoutInflater): ActivityImagePreviewBinding {
        return ActivityImagePreviewBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
        lifecycleScope.launchWhenStarted {
            downloadViewModel.downloadCompleted
                .doOnSuccess {
                    showToast(
                        getString(R.string.format_download_success, it.absolutePath),
                        Toast.LENGTH_LONG
                    )
                }
                .doOnFailure {
                    showToast(getString(R.string.format_download_failure, it?.message))
                }
                .collect()
        }
    }
}