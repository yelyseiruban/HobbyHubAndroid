package com.yelysei.hobbyharbor.model.userhobbies.entities

data class Experience(
    val id: Int,
    val startTime: Long,
    val endTime: Long,
    val note: String? = null
)
