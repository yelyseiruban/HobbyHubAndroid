package com.yelysei.hobbyharbor.model.hobbies.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby

/**
 * Entity for hobbies table, hobbyName in table is unique,
 * so user could not add hobby with existing hobbyName
 */
@Entity(
    tableName = "hobbies",
    indices = [
        Index("hobby_name", unique = true)
    ]
)
data class HobbyDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "hobby_name") val hobbyName: String,
    @ColumnInfo(name = "category_name") val categoryName: String,
    val cost: String,
    val place: String,
    val people: String
) {
    fun toHobby(): Hobby = Hobby(
        id,
        hobbyName,
        categoryName,
        cost,
        place,
        people
    )

    companion object {
        fun fromHobby(hobby: Hobby): HobbyDbEntity = HobbyDbEntity(
            id = 0,
            hobbyName = hobby.hobbyName,
            categoryName = hobby.categoryName,
            cost = hobby.cost,
            place = hobby.place,
            people = hobby.people
        )
    }
}