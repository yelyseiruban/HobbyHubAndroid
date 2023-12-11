package com.yelysei.hobbyharbor.model.userhobbies.entities

import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby

data class UserHobby(
    val id: Int,
    val hobby: Hobby,
    val progress: Progress
)

fun UserHobby.getProgressInHours() : Float {
    var progressInMilliseconds = 0f
    this.progress.history.forEach { action ->
        progressInMilliseconds += (action.endTime - action.startTime)
    }
    return progressInMilliseconds / 3600000
}