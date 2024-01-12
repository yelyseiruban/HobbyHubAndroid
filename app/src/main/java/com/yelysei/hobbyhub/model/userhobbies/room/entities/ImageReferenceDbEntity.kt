package com.yelysei.hobbyhub.model.userhobbies.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.yelysei.hobbyhub.model.userhobbies.entities.ImageReference

@Entity(
    tableName = "image_references",
    foreignKeys = [
        ForeignKey(
            entity = ExperienceDbEntity::class,
            parentColumns = ["id"],
            childColumns = ["experience_id"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE
        )
    ]
)
class ImageReferenceDbEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "uri_reference") val uriReference: String,
    @ColumnInfo(name = "experience_id") val experienceId: Int
) {

    fun toImageReference(): ImageReference {
        return ImageReference(
            id = id,
            uriReference = uriReference,
        )
    }

    companion object {
        fun fromImageReference(
            imageReference: ImageReference,
            experienceId: Int = 0
        ): ImageReferenceDbEntity {
            return ImageReferenceDbEntity(
                id = imageReference.id,
                uriReference = imageReference.uriReference,
                experienceId = experienceId
            )
        }
    }

}