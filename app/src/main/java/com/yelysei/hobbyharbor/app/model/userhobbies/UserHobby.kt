package com.yelysei.hobbyharbor.app.model.userhobbies

import com.yelysei.hobbyharbor.app.model.hobbies.Hobby
import java.sql.Date

data class UserHobby(
    val id: Long,
    val hobby: Hobby,
    val progress: Progress
)

data class Progress (
    val history: MutableList<Action>,
    val goal: Int,
    val currentProgress: Int
)

data class Action (
    val date: Date,
    val startTime: Time,
    val endTime: Time
)

data class Time(
    val hour: Int,
    val minute: Int
)


