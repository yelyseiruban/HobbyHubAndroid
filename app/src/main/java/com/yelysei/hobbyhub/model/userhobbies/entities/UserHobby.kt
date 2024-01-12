package com.yelysei.hobbyhub.model.userhobbies.entities

import com.yelysei.hobbyhub.model.hobbies.entities.Hobby
import kotlin.math.round

data class UserHobby(
    val id: Int,
    val hobby: Hobby,
    var progress: Progress
)

fun UserHobby.getProgressInHours(): Float {
    var progressInMilliseconds = 0f
    this.progress.experiences.forEach { action ->
        progressInMilliseconds += (action.endTime - action.startTime)
    }
    return (progressInMilliseconds / 3600000).round(1)
}

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0f
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}


