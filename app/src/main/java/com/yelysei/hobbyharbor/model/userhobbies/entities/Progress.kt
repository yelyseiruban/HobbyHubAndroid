package com.yelysei.hobbyharbor.model.userhobbies.entities

data class Progress (
    val id: Int,
    var actions: List<Action>,
    var goal: Int
)
