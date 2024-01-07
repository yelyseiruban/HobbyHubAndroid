package com.yelysei.hobbyharbor.model.userhobbies.entities

data class Progress(
    val id: Int,
    var experiences: List<Experience>,
    var goal: Int
)
