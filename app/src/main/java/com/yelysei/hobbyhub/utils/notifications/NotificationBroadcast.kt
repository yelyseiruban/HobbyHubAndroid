package com.yelysei.hobbyhub.utils.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.yelysei.hobbyhub.R
import com.yelysei.hobbyhub.ui.screens.main.MainActivity


class NotificationBroadcast: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val channelId = intent.getStringExtra(EXTRA_CHANNEL_ID) ?: throw IllegalStateException("Channel id must be specified")
        val notificationId = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)
        val notificationTitle = intent.getStringExtra(EXTRA_NOTIFICATION_TITLE) ?: throw IllegalStateException("Title must be specified")
        val notificationContent = intent.getStringExtra(EXTRA_NOTIFICATION_CONTENT) ?: throw IllegalStateException("Content must be specified")
        val requestCode = intent.getIntExtra(EXTRA_NOTIFICATION_ID, 0)


        val builder = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.mipmap.ic_launcher_foreground)
            .setContentTitle(notificationTitle)
            .setContentText(notificationContent)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(
                PendingIntent.getActivity(
                    context,
                    requestCode,
                    Intent(context, MainActivity::class.java),
                    PendingIntent.FLAG_IMMUTABLE
                )
            )

        val notificationManager = NotificationManagerCompat.from(context)

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        notificationManager.notify(notificationId, builder.build())
    }

    companion object {
        const val EXTRA_CHANNEL_ID = "channelId"
        const val EXTRA_NOTIFICATION_ID = "notificationId"
        const val EXTRA_NOTIFICATION_TITLE = "notificationTitle"
        const val EXTRA_NOTIFICATION_CONTENT = "notificationContent"
    }
}