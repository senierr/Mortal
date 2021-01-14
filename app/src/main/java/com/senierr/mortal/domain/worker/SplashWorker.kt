package com.senierr.mortal.domain.worker

import android.content.Context
import androidx.work.*
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.senierr.repository.Repository
import com.senierr.repository.service.api.IGankService
import kotlinx.coroutines.coroutineScope
import java.util.concurrent.TimeUnit

/**
 * 引导页推荐图后台缓存任务
 *
 * @author zhouchunjie
 * @date 2020/5/19
 */
class SplashWorker(context: Context, parameters: WorkerParameters) : CoroutineWorker(context, parameters) {

    companion object {
        const val TAG_WORK_SPLASH = "worker_splash"

        fun start(context: Context) {
            // 创建循环任务请求
            val workRequest = OneTimeWorkRequestBuilder<SplashWorker>()
                    .setConstraints(
                            Constraints.Builder()
                                    .setRequiredNetworkType(NetworkType.CONNECTED)
                                    .build()
                    )
                    .setBackoffCriteria(
                            BackoffPolicy.LINEAR,
                            PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                            TimeUnit.MILLISECONDS
                    ).build()
            // 执行任务
            WorkManager.getInstance(context)
                    .enqueueUniqueWork(TAG_WORK_SPLASH, ExistingWorkPolicy.REPLACE, workRequest)
        }

        fun cancel(context: Context) {
            WorkManager.getInstance(context).cancelUniqueWork(TAG_WORK_SPLASH)
        }
    }

    private val gankService = Repository.getService<IGankService>()

    override suspend fun doWork(): Result = coroutineScope {
        try {
            val girl = gankService.getRandomGirls(1).firstOrNull()
            // 预加载图片
            girl?.let {
                Glide.with(applicationContext)
                        .load(it.url)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .preload()
            }
            Result.success()
        } catch (e: Exception) {
            Result.failure()
        }
    }
}