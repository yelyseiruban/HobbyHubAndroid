package com.yelysei.hobbyharbor.app.model.hobbies

data class Hobby(
    val id: Long,
    val hobbyName: String,
    val categoryName: String,
    val cost: String,
    val place: String,
    val people: String,
)