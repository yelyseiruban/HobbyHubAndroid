package com.yelysei.hobbyhub.model.hobbies.entities

data class Hobby(
    var id: Int? = null,
    val hobbyName: String,
    val categoryName: String,
    val cost: String?,
    val place: String?,
    val people: String?,
)