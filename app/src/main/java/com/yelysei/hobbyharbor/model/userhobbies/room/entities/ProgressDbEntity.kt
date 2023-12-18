package com.yelysei.hobbyharbor.model.userhobbies.room.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yelysei.hobbyharbor.model.userhobbies.entities.Progress

@Entity(
    tableName = "progresses",
)
data class ProgressDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val goal: Int
) {
    fun toProgress(): Progress {
        return Progress (
            id,
            mutableListOf(),
            goal
        )
    }
}