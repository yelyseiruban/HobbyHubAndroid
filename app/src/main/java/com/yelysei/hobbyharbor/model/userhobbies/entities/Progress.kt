package com.yelysei.hobbyharbor.model.userhobbies.entities

data class Progress (
    val id: Int,
    var history: MutableList<Action>,
    val goal: Int
)
