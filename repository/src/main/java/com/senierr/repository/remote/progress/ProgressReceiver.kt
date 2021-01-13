package com.senierr.repository.remote.progress

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.senierr.repository.Repository

/**
 * 进度广播接收器
 *
 * @author chunjiezhou
 * @date 2021/01/13
 */
abstract class ProgressReceiver: BroadcastReceiver() {

    companion object {
        const val ACTION_REMOTE_PROGRESS_RECEIVER = "repository.remote.progress.receiver"

        private const val KEY_TAG = "key_tag"
        private const val KEY_PROGRESS = "key_progress"

        /**
         * 发送进度
         *
         * @param tag 标签
         * @param progress 进度
         */
        fun sendProgress(tag: String, progress: Progress) {
            Repository.getApplication()
                    .sendBroadcast(Intent(ACTION_REMOTE_PROGRESS_RECEIVER).apply {
                        putExtra(KEY_TAG, tag)
                        putExtra(KEY_PROGRESS, progress)
                    })
        }
    }

    /**
     * 进度回调
     *
     * @param context 上下文
     * @param tag 标签
     * @param progress 进度
     */
    abstract fun onProgress(context: Context, tag: String, progress: Progress)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (context != null && intent != null && intent.action == ACTION_REMOTE_PROGRESS_RECEIVER) {
            val tag = intent.getStringExtra(KEY_TAG)
            val progress = intent.getParcelableExtra<Progress>(KEY_PROGRESS)
            if (tag != null && progress != null) {
                onProgress(context, tag, progress)
            }
        }
    }
}