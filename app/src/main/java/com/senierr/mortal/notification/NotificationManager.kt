package com.senierr.mortal.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.senierr.base.support.utils.LogUtil
import com.senierr.mortal.R
import com.senierr.mortal.domain.main.MainActivity

/**
 * 通知管理
 *
 * @author zhouchunjie
 * @date 2019/7/2
 */
object NotificationManager {

    private var channelCreated = false

    // 默认渠道ID
    private const val CHANNEL_ID_DEFAULT = "100"
    // 升级更新渠道ID
    private const val CHANNEL_ID_UPDATE = "101"

    // 升级更新通知ID
    const val NOTIFY_ID_UPDATE = 0

    /**
     * 创建通知渠道
     */
    private fun createNotificationChannel(context: Context) {
        if (channelCreated) return
        val notificationManagerCompat = NotificationManagerCompat.from(context)
        // 创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 默认通知
            val channelDefault = NotificationChannel(
                CHANNEL_ID_DEFAULT,
                context.getString(R.string.channel_name_default),
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channelDefault.description = context.getString(R.string.channel_description_default)
            // 升级进度通知
            val channelUpdate = NotificationChannel(
                CHANNEL_ID_UPDATE,
                context.getString(R.string.channel_name_update),
                NotificationManager.IMPORTANCE_LOW
            )
            channelUpdate.description = context.getString(R.string.channel_description_update)
            // 创建多个渠道
            notificationManagerCompat.createNotificationChannels(
                mutableListOf(channelDefault, channelUpdate)
            )
        }
        channelCreated = true
    }

    /**
     * 取消{@notifyId}通知
     */
    fun cancel(context: Context, notifyId: Int) {
        NotificationManagerCompat.from(context).cancel(notifyId)
    }

    /**
     * 取消所有通知
     */
    fun cancelAll(context: Context) {
        NotificationManagerCompat.from(context).cancelAll()
    }

    /**
     * 发送更新通知
     */
    fun sendDownloadNotification(context: Context, percent: Int) {
        createNotificationChannel(context)
        val builder = NotificationCompat.Builder(context, CHANNEL_ID_UPDATE).apply {
            setContentTitle(context.getString(R.string.app_name))
            setSmallIcon(R.mipmap.ic_launcher)
            setWhen(System.currentTimeMillis())
            priority = NotificationCompat.PRIORITY_LOW
            setAutoCancel(true)
            setOnlyAlertOnce(true)
        }
        if (percent in 0 until 100) {
            builder.setContentText(context.getString(R.string.download_ing))
            // 设置为false，表示刻度，设置为true，表示流动
            builder.setProgress(100, percent, false)
        } else {
            builder.setContentText(context.getString(R.string.download_completed))
            // 0,0,false,可以将进度条隐藏
            builder.setProgress(0, 0, false)
        }
        NotificationManagerCompat.from(context).notify(NOTIFY_ID_UPDATE, builder.build())
    }

    /**
     * 发送默认通知
     */
    fun sendDefaultNotification(context: Context, notifyId: Int, title: String, content: String, intent: PendingIntent) {
        createNotificationChannel(context)
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_DEFAULT)
        notification.setContentTitle(title)
        notification.setContentText(content)
        notification.setContentIntent(intent)
        notification.setSmallIcon(R.mipmap.ic_launcher)
        notification.setWhen(System.currentTimeMillis())
        notification.priority = NotificationCompat.PRIORITY_DEFAULT
        notification.setAutoCancel(true)
        NotificationManagerCompat.from(context).notify(notifyId, notification.build())
    }
}