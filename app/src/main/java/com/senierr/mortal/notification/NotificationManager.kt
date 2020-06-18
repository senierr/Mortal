package com.senierr.mortal.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.senierr.mortal.R

/**
 * 通知管理
 *
 * @author zhouchunjie
 * @date 2019/7/2
 */
object NotificationManager {

    // 默认渠道
    private const val CHANNEL_ID_DEFAULT = "100"
    private const val CHANNEL_NAME_DEFAULT = "Default"
    // 升级进度渠道
    private const val CHANNEL_ID_UPDATE_PROGRESS = "101"
    private const val CHANNEL_NAME_UPDATE_PROGRESS = "Update Notification"

    /**
     * 获取通知管理
     */
    private fun getNotificationManager(context: Context): NotificationManager? {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?
        // 创建通知渠道
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 默认通知
            val channelDefault = NotificationChannel(
                CHANNEL_ID_DEFAULT, CHANNEL_NAME_DEFAULT,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager?.createNotificationChannel(channelDefault)
            // 升级进度通知
            val channelUpdate = NotificationChannel(
                CHANNEL_ID_UPDATE_PROGRESS, CHANNEL_NAME_UPDATE_PROGRESS,
                NotificationManager.IMPORTANCE_LOW
            )
            notificationManager?.createNotificationChannel(channelUpdate)
        }
        return notificationManager
    }

    /**
     * 发送默认通知
     */
    fun sendDefaultNotification(context: Context, notifyId: Int, title: String, content: String, intent: PendingIntent) {
        val notification = NotificationCompat.Builder(context, CHANNEL_ID_DEFAULT)
        notification.setContentTitle(title)
        notification.setContentText(content)
        notification.setContentIntent(intent)
        notification.setSmallIcon(R.mipmap.ic_launcher)
        notification.setWhen(System.currentTimeMillis())
        notification.setAutoCancel(true)
        getNotificationManager(context)?.notify(notifyId, notification.build())
    }

    /**
     * 发送更新通知
     */
    fun sendUpdateNotification(context: Context, title: String, content: String, progress: Int) {
        val builder = NotificationCompat.Builder(context, CHANNEL_ID_UPDATE_PROGRESS)
        builder.setContentTitle(title)
        builder.setContentText(content)
        builder.setSmallIcon(R.mipmap.ic_launcher)
        builder.setWhen(System.currentTimeMillis())
        builder.setAutoCancel(true)
        if (progress in 0 until 100) {
            //设置为false，表示刻度，设置为true，表示流动
            builder.setProgress(100, progress, false)
        } else {
            //0,0,false,可以将进度条隐藏
            builder.setProgress(0, 0, false)
        }
        getNotificationManager(context)?.notify(0, builder.build())
    }

    /**
     * 取消{@notifyId}通知
     */
    fun cancel(context: Context, notifyId: Int) {
        getNotificationManager(context)?.cancel(notifyId)
    }

    /**
     * 取消所有通知
     */
    fun cancelAll(context: Context) {
        getNotificationManager(context)?.cancelAll()
    }
}