package com.yelysei.hobbyhub.model.userhobbies.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yelysei.hobbyhub.model.userhobbies.entities.Experience

@Entity(
    tableName = "experiences",
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
data class ExperienceDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "start_date") val startDate: Long,
    @ColumnInfo(name = "end_date") val endDate: Long,
    @ColumnInfo(name = "progress_id") val progressId: Int,
    @ColumnInfo(name = "note") val note: String?
) {

    fun toExperience(): Experience {
        return Experience(
            id = id,
            startTime = startDate,
            endTime = endDate,
            note = note
        )
    }

    companion object {
        fun fromExperience(experience: Experience, progressId: Int): ExperienceDbEntity {
            return ExperienceDbEntity(
                id = experience.id,
                startDate = experience.startTime,
                endDate = experience.endTime,
                progressId = progressId,
                note = experience.note
            )
        }
    }
}