package com.senierr.mortal.worker

import android.content.Context
import androidx.work.*
import com.senierr.base.support.utils.LogUtil
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

        fun start(context: Context, url: String): OneTimeWorkRequest {
            // 创建触发限制条件
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            // 创建循环任务请求
            val workRequest = OneTimeWorkRequest.Builder(DownloadWorker::class.java)
                .setConstraints(constraints)
                .setInputData(
                    Data.Builder()
                        .putString(KEY_URL, url)
                        .build()
                )
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


        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        LogUtil.logE("onStopped")
    }
}