package com.yelysei.hobbyharbor.model.userhobbies.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action

@Entity(
    tableName = "actions",
    foreignKeys = [
        ForeignKey(
            entity = ProgressDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["progress_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
data class ActionDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "start_date") val startDate: Long,
    @ColumnInfo(name = "end_date") val endDate: Long,
    @ColumnInfo(name = "progress_id") val progressId: Int
) {

    fun toAction(): Action {
        return Action(
            id = id,
            startTime = startDate,
            endTime = endDate
        )
    }
    companion object {
        fun fromAction(action: Action, progressId: Int): ActionDbEntity {
            return ActionDbEntity(
                id = 0,
                startDate = action.startTime,
                endDate = action.endTime,
                progressId = progressId
            )
        }
    }
}