package com.yelysei.hobbyharbor.model.userhobbies.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "progresses",
)
data class ProgressDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val goal: Int
) {
}