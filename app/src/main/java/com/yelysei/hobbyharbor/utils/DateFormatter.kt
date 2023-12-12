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
fun getDate(milliSeconds: Int, dateFormat: String): String {

    // Create a DateFormatter object for displaying date in specified format.

    val formatter = SimpleDateFormat(dateFormat)

    // Create a calendar object that will convert the date and time value in milliseconds to date.

    val calendar: Calendar = Calendar.getInstance()
    calendar.setTimeInMillis(milliSeconds.toLong())
    return formatter.format(calendar.getTime())
}