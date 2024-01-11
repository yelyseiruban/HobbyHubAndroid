package com.yelysei.hobbyharbor.utils

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SharedStorage(
    private val sharedPreferences: SharedPreferences
) {

    private fun storeNotificationsStateList(list: List<NotificationState>) {
        val gson = Gson()
        val json = gson.toJson(list)
        sharedPreferences.edit()
            .putString(NOTIFICATIONS_STATE_KEY, json)
            .apply()
    }

    fun changeOrAddNotificationStateObject(notificationState: NotificationState) {
        val oldNotificationStateList = getNotificationStateList().toMutableList()
        val oldNotificationStateIndex = oldNotificationStateList.indexOfFirst { it.hobbyName == notificationState.hobbyName }
        if (oldNotificationStateIndex != -1) {
            //change functionality
            oldNotificationStateList[oldNotificationStateIndex] = notificationState
        } else {
            //add functionality
            oldNotificationStateList.add(notificationState)
        }
        storeNotificationsStateList(oldNotificationStateList)
    }

    private fun getNotificationStateList(): List<NotificationState> {
        val notificationsStateJson = sharedPreferences.getString(NOTIFICATIONS_STATE_KEY, null)
            ?: "[]"
        storeNotificationsStateList(listOf())

        val gson = Gson()

        val notificationsStateListType = object : TypeToken<List<NotificationState>>() {}.type
        return gson.fromJson(notificationsStateJson, notificationsStateListType)
    }

    fun removeNotificationByHobbyName(hobbyName: String) {
        val oldNotificationStateList = getNotificationStateList().toMutableList()
        oldNotificationStateList.removeIf { it.hobbyName == hobbyName }
        storeNotificationsStateList(oldNotificationStateList)
    }

    fun isNotificationActivated(hobbyName: String): Boolean {
        val notificationState = getNotificationStateList().find { it.hobbyName == hobbyName } ?: return false
        return notificationState.isActive
    }

    fun getActiveNotificationStateList(): List<NotificationState> {
        return getNotificationStateList().filter { it.isActive == true }
    }

    fun getInternalTimeInMillisecondsByHobbyName(hobbyName: String): Long? {
        val notificationState = getNotificationStateList().find { it.hobbyName == hobbyName } ?: return null
        return notificationState.internalTimeInMilliseconds
    }

    companion object {
        const val STORE_FILE_NAME = "notifications_info"
        const val NOTIFICATIONS_STATE_KEY = "notifications_state"
    }
}

data class NotificationState(
    val hobbyName: String,
    val channelId: String,
    val notificationId: Int,
    val internalTimeInMilliseconds: Long,
    var isActive: Boolean,
)