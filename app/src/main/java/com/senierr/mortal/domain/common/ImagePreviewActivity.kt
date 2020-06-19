package com.senierr.mortal.domain.common

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.WorkManager
import com.bm.library.PhotoView
import com.senierr.adapter.internal.ViewHolder
import com.senierr.base.support.ext.click
import com.senierr.base.support.ui.BaseActivity
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.common.vm.DownloadViewModel
import com.senierr.mortal.ext.show
import com.senierr.mortal.worker.DownloadWorker
import kotlinx.android.synthetic.main.activity_image_preview.*

/**
 * 图片预览页面
 *
 * @author zhouchunjie
 * @date 2019/5/30 10:03
 */
class ImagePreviewActivity : BaseActivity(R.layout.activity_image_preview) {

    companion object {
        private const val KEY_IMAGES = "images"

        fun start(context: Context, images: MutableList<String>) {
            val intent = Intent(context, ImagePreviewActivity::class.java)
            intent.putStringArrayListExtra(KEY_IMAGES, arrayListOf(*images.toTypedArray()))
            context.startActivity(intent)
        }
    }

    private lateinit var downloadViewModel: DownloadViewModel

    private val images = mutableListOf<String>()
    private val imagePreviewAdapter = object : androidx.viewpager.widget.PagerAdapter() {
        override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1

        override fun getCount(): Int = images.size

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val holder = ViewHolder.create(container, R.layout.item_image_preview)
            val pvPreview = holder.findView<PhotoView>(R.id.pv_preview)

            pvPreview?.click {
//                downloadViewModel.download(images[position], "adasdadas.jpg")
                val request = DownloadWorker.start(this@ImagePreviewActivity, images[position], "adasdadas.jpg")
                WorkManager.getInstance(this).getWorkInfoByIdLiveData(request.getId())
                    .observe(this, object : Observer<WorkInfo>{
                        override fun onChanged(@Nullable workInfo: WorkInfo?) {
                            if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                                //获取成功返回的结果
                                Log.e("TAG",workInfo.outputData.getString(DATA_KEY))
                            }
                        }
                    })
            }

            pvPreview?.enable()
            pvPreview?.show(images[position])

            container.addView(holder.itemView)
            return holder.itemView
        }

        override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
            container.removeView(view as View)
        }
    }

    //广播监听下载的各个状态
    private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent?) {
            val query = DownloadManager.Query()
            //通过下载的id查找
            query.setFilterById(id)
            val downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
            val cursor: Cursor = downloadManager?.query(query)
            if (cursor.moveToFirst()) {
                val status: Int = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
                when (status) {
                    DownloadManager.STATUS_PAUSED -> {

                    }
                    DownloadManager.STATUS_PENDING -> {

                    }
                    DownloadManager.STATUS_RUNNING -> {

                    }
                    DownloadManager.STATUS_SUCCESSFUL -> {
                        cursor.close()
                        context.unregisterReceiver(this)
                    }
                    DownloadManager.STATUS_FAILED -> {
                        cursor.close()
                        context.unregisterReceiver(this)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initParams()
        initView()
        initViewModel()

        //注册广播接收者，监听下载状态
        registerReceiver(receiver, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        super.onDestroy()
    }

    private fun initParams() {
        val temp = intent.getStringArrayListExtra(KEY_IMAGES)
        images.addAll(temp)
    }

    private fun initView() {
        btn_close?.click { finish() }
        vp_preview?.adapter = imagePreviewAdapter
    }

    private fun initViewModel() {
        downloadViewModel = ViewModelProvider(this).get(DownloadViewModel::class.java)
        downloadViewModel.downloadProgress.observe(this, Observer {
            LogUtil.logE("progress: ${it.percent}")
        })
        downloadViewModel.downloadResult.observe(this, {
            LogUtil.logE("success: ${it.absolutePath}")
        }, {
            LogUtil.logE("failure: ${it.message}")
        })
    }
}