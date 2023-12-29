package com.yelysei.hobbyharbor.model.userhobbies.room.entities

import androidx.room.ColumnInfo
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Progress
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby

data class UserHobbiesInTuple(
    val id: Int,
    @ColumnInfo(name = "hobby_id") val hobbyId: Int,
    @ColumnInfo(name = "progress_id") val progressId: Int,
    @ColumnInfo(name = "hobby_name") val hobbyName: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    val cost: String?,
    val place: String?,
    val people: String?,
    val goal: Int
) {
    fun toUserHobby(): UserHobby {
        return UserHobby(
            id,
            Hobby(
                hobbyId,
                hobbyName,
                categoryName,
                cost,
                place,
                people
            ),
            Progress(
                progressId,
                mutableListOf(),
                goal
            )
        )
    }
}