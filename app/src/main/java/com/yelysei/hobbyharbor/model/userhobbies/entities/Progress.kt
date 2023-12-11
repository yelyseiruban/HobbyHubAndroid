package com.yelysei.hobbyharbor.model.userhobbies.entities

data class Progress (
    val id: Int,
    val history: MutableList<Action>,
    val goal: Int
)
