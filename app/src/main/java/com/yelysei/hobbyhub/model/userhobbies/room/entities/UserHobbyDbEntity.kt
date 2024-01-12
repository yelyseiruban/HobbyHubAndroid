package com.yelysei.hobbyhub.model.userhobbies.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yelysei.hobbyhub.model.hobbies.room.entities.HobbyDbEntity
import com.yelysei.hobbyhub.model.userhobbies.entities.UserHobby


/**
 * Entity for user_hobbies table, hobby_id and progress_id are unique,
 * so User could not add two hobbies which are the same,
 * and to one one progress should be for one hobby
 */
@Entity(
    tableName = "user_hobbies",
    indices = [
        Index("hobby_id", unique = true),
        Index("progress_id", unique = true)
    ],
    foreignKeys = [
        ForeignKey(
            entity = HobbyDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["hobby_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ProgressDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["progress_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class UserHobbyDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "hobby_id") val hobbyId: Int,
    @ColumnInfo(name = "progress_id") val progressId: Int
) {
    companion object {
        fun fromUserHobby(userHobby: UserHobby): UserHobbyDbEntity {
            return UserHobbyDbEntity(
                id = userHobby.id,
                hobbyId = userHobby.hobby.id ?: throw Exception("Hobby does not have id"),
                progressId = userHobby.progress.id
            )
        }
    }

}