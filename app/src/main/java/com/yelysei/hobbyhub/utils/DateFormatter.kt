package com.yelysei.hobbyhub.utils

/**
 * Copied from https://stackoverflow.com/questions/7953725/how-to-convert-milliseconds-to-date-format-in-android
 * Return date in specified format.
 * @param milliSeconds Date in milliseconds
 * @param dateFormat Date format
 * @return String representing date in specified format
 */
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.TimeZone

fun getFormattedDate(milliSeconds: Long, dateFormat: String, timeZone: TimeZone = TimeZone.getTimeZone("UTC")): String {
    val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
    formatter.timeZone = timeZone

    val calendar: Calendar = Calendar.getInstance()
    calendar.timeInMillis = milliSeconds

    return formatter.format(calendar.time)
}

fun getDate(milliSeconds: Long): String {
    return getFormattedDate(milliSeconds, "dd.MM.yyyy")
}

fun getTime(milliSeconds: Long): String {
    return getFormattedDate(milliSeconds, "HH:mm")
}
