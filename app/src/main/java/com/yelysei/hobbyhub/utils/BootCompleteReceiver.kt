package com.yelysei.hobbyhub.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.yelysei.hobbyhub.SharedPreferences
import com.yelysei.hobbyhub.utils.notifications.NotificationBroadcast
import java.util.Calendar

class BootCompleteReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent?.action == "android.intent.action.BOOT_COMPLETED") {

            // set your alarms here
            val sharedStorage = SharedPreferences.sharedStorage
            sharedStorage.getActiveNotificationStateList().forEach {
                val channelId = it.channelId
                val notificationId = it.notificationId
                val hobbyName = it.hobbyName
                val requestCode = it.notificationId
                val intervalInMilliseconds = it.internalTimeInMilliseconds

                val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                val intent = Intent(context, NotificationBroadcast::class.java).apply {
                    putExtra(NotificationBroadcast.EXTRA_CHANNEL_ID, channelId)
                    putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_ID, notificationId)
                    putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_TITLE, CustomTypeface.capitalizeEachWord(hobbyName))
                    putExtra(NotificationBroadcast.EXTRA_NOTIFICATION_CONTENT, "It is time to do ${CustomTypeface.capitalizeEachWord(hobbyName)}")
                }
                val pendingIntent = PendingIntent.getBroadcast(context, requestCode, intent,
                    PendingIntent.FLAG_IMMUTABLE
                )

                val calendar = Calendar.getInstance()
                calendar.timeInMillis = System.currentTimeMillis()

                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.timeInMillis,
                    intervalInMilliseconds,
                    pendingIntent
                )
            }
        }
    }
}