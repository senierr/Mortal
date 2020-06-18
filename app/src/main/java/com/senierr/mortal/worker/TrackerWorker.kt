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
class TrackerWorker(context: Context, parameters: WorkerParameters) : Worker(context, parameters) {

    companion object {
        private const val TAG_WORK_TRACKER = "worker_tracker"
        private const val DELAY_SECONDS = 5L

        fun start(context: Context) {
            // 创建循环任务请求
            val workRequest = OneTimeWorkRequest.Builder(TrackerWorker::class.java)
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build()
                )
                .setInitialDelay(DELAY_SECONDS, TimeUnit.SECONDS)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                ).build()
            // 执行任务
            WorkManager.getInstance(context)
                .enqueueUniqueWork(TAG_WORK_TRACKER, ExistingWorkPolicy.REPLACE, workRequest)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(TAG_WORK_TRACKER)
        }
    }

    override fun doWork(): Result {
        for (i in 0..5) {
            if (isStopped) break
            LogUtil.logE("doWork: ${Thread.currentThread().name} - $i")
            Thread.sleep(1000)
        }
        // 循环启动
        start(applicationContext)
        return Result.success()
    }

    override fun onStopped() {
        super.onStopped()
        LogUtil.logE("onStopped")
    }
}