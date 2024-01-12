package com.yelysei.hobbyhub.utils

object DisplayedDateTime {
    /**
     * @return String representing date borders or just date
     * date - date or just date
     */
    fun displayedSingleDate(startTime: Long, endTime: Long): String {
        if (getDateFormat(startTime, endTime) == DateFormat.SINGLE_DATE) {
            return getDate(startTime)
        }
        throw IncorrectDateFormatException()
    }

    /**
     * @return String representing time borders
     * time - time
     */

    fun displayedTime(startTime: Long, endTime: Long): String {
        return "${getTime(startTime)} - ${getTime(endTime)}"
    }


    /**
     * @retrun String representing datetime borders
     * date (time) - date (time)
     */
    fun displayedDateTime(time: Long): String {
        return "${getDate(time)} (${getTime(time)})"
    }

    fun getDateFormat(startTime: Long, endTime: Long) : DateFormat {
        val startDate = getDate(startTime)
        val endDate = getDate(endTime)
        return if (startDate != endDate) {
            DateFormat.DOUBLE_DATES
        } else {
            DateFormat.SINGLE_DATE
        }
    }

}

enum class DateFormat {
    SINGLE_DATE, DOUBLE_DATES
}

class IncorrectDateFormatException : RuntimeException()