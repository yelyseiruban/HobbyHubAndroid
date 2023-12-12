package com.yelysei.hobbyharbor.model.userhobbies.entities

data class Progress (
    val id: Int,
    var actions: MutableList<Action>,
    val goal: Int
)
