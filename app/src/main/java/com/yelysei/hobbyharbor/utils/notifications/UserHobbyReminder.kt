package com.yelysei.hobbyharbor.utils.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.yelysei.hobbyharbor.utils.CustomTypeface

object UserHobbyReminder {
    fun removeNotification(context: Context, hobbyName: String, hobbyId: Int) {
        val intent = Intent(context, NotificationBroadcast::class.java).apply {
            putExtra(NotificationBroadcast.EXTRA_CHANNEL_ID, "channelId $hobbyName")
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_ID, hobbyId)
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_TITLE, CustomTypeface.capitalizeEachWord(hobbyName))
            putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_CONTENT, "It is time to do ${CustomTypeface.capitalizeEachWord(hobbyName)}")
        }
        val pendingIntent = PendingIntent.getBroadcast(context, hobbyId, intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
        pendingIntent.cancel()
    }
}