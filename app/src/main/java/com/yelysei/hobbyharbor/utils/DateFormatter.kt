package com.yelysei.hobbyharbor.utils

import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Copied from https://stackoverflow.com/questions/7953725/how-to-convert-milliseconds-to-date-format-in-android
 * Return date in specified format.
 * @param milliSeconds Date in milliseconds
 * @param dateFormat Date format
 * @return String representing date in specified format
 */
fun getFormattedDate(milliSeconds: Long, dateFormat: String): String {

    // Create a DateFormatter object for displaying date in specified format.

    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.

    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(milliSeconds)
    return formatter.format(calendar.getTime())
}

fun getTimeInMillis(year: Int, month: Int, day: Int, hour: Int, minute: Int): Long {
    val calendar = Calendar.getInstance()
    calendar.set(Calendar.YEAR, year)
    calendar.set(Calendar.MONTH, month - 1) // Calendar months are zero-based
    calendar.set(Calendar.DAY_OF_MONTH, day)
    calendar.set(Calendar.HOUR_OF_DAY, hour)
    calendar.set(Calendar.MINUTE, minute)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MILLISECOND, 0)

    return calendar.timeInMillis
}


fun getDate(milliSeconds: Long): String {
    return getFormattedDate(milliSeconds, "dd.MM.yyyy")
}

fun getTime(milliSeconds: Long): String {
    return getFormattedDate(milliSeconds, "HH:mm")
}
