package com.senierr.mortal.domain.worker

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * 数据追踪后台任务
 *
 * @author zhouchunjie
 * @date 2020/5/19
 */
class TrackerWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    companion object {
        private const val TAG_WORK_TRACKER = "worker_tracker"

        fun start(context: Context) {
            // 创建循环任务请求
            val workRequest = PeriodicWorkRequestBuilder<TrackerWorker>(5, TimeUnit.SECONDS)
                    .setConstraints(
                            Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                    )
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .setBackoffCriteria(
                            BackoffPolicy.LINEAR,
                            PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                            TimeUnit.MILLISECONDS
                    ).build()
            // 执行任务
            WorkManager.getInstance(context)
                    .enqueueUniquePeriodicWork(TAG_WORK_TRACKER, ExistingPeriodicWorkPolicy.REPLACE, workRequest)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(TAG_WORK_TRACKER)
        }
    }

    override suspend fun doWork(): Result {
        // TODO 数据上传
        return Result.success()
    }
}