package com.senierr.mortal.worker

import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.database.Cursor
import android.net.Uri
import android.os.Environment
import androidx.work.*
import com.senierr.base.support.utils.LogUtil
import java.io.File
import java.util.concurrent.TimeUnit

/**
 * 数据追踪后台任务
 *
 * @author zhouchunjie
 * @date 2020/5/19
 */
class DownloadWorker(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    companion object {
        private const val TAG_DOWNLOAD = "worker_download"
        private const val KEY_URL = "key_url"
        private const val KEY_DEST_NAME = "key_dest_name"
        private const val KEY_DOWNLOAD_ID = "key_download_id"

        fun start(context: Context, url: String, destName: String): OneTimeWorkRequest {
            // 创建触发限制条件
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            // 创建参数
            val inputData = Data.Builder()
                .putString(KEY_URL, url)
                .putString(KEY_DEST_NAME, destName)
                .build()
            // 创建循环任务请求
            val workRequest = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
                .setConstraints(constraints)
                .setInputData(inputData)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()
            // 执行任务
            WorkManager.getInstance(context).enqueue(workRequest)
            return workRequest
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(TAG_DOWNLOAD)
        }
    }

    override fun doWork(): Result {
        val url = inputData.getString(KEY_URL)
        val destName = inputData.getString(KEY_DEST_NAME)

        val request = DownloadManager.Request(Uri.parse(url))
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
        request.setTitle(destName)
        request.setDescription("文件正在下载中......")
        request.setVisibleInDownloadsUi(true)

        //设置下载的路径
        val file = File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), destName)
        request.setDestinationUri(Uri.fromFile(file))
        //获取DownloadManager
        val downloadManager = applicationContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager?
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        val downloadId = downloadManager?.enqueue(request)

        // 输出返回值
        val outPutData = Data.Builder()
            .putString(KEY_DOWNLOAD_ID, downloadId.toString())
            .build()
        return Result.success(outPutData)
    }

    override fun onStopped() {
        super.onStopped()
        LogUtil.logE("onStopped")
    }
}