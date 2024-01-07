package com.yelysei.hobbyharbor.model.userhobbies.room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ExperienceDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ImageReferenceDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.UserHobbiesInTuple
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.UserHobbyDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserHobbiesDao {

    @Query(
        "SELECT user_hobbies.id, user_hobbies.hobby_id, user_hobbies.progress_id, " +
                "hobbies.hobby_name, hobbies.category_name, hobbies.cost, hobbies.place, hobbies.people, progresses.goal " +
                "FROM user_hobbies " +
                "JOIN hobbies " +
                "ON user_hobbies.hobby_id = hobbies.id " +
                "JOIN progresses " +
                "ON user_hobbies.progress_id = progresses.id"
    )
    fun getUserHobbies(): Flow<List<UserHobbiesInTuple>>

    @Query(
        "SELECT user_hobbies.id, user_hobbies.hobby_id, user_hobbies.progress_id, " +
                "hobbies.hobby_name, hobbies.category_name, hobbies.cost, hobbies.place, hobbies.people, progresses.goal " +
                "FROM user_hobbies " +
                "JOIN hobbies " +
                "ON user_hobbies.hobby_id = hobbies.id " +
                "JOIN progresses " +
                "ON user_hobbies.progress_id = progresses.id " +
                "WHERE user_hobbies.id = :id"
    )
    fun findUserHobbyById(id: Int): Flow<UserHobbiesInTuple>

    @Query(
        "SELECT * FROM progresses WHERE progresses.id = :id"
    )
    fun findProgressById(id: Int): Flow<ProgressDbEntity>

    @Insert
    suspend fun addUserHobby(userHobby: UserHobbyDbEntity)

    @Query("SELECT * FROM experiences WHERE experiences.progress_id = :progressId")
    fun findExperiencesByProgressId(progressId: Int): Flow<List<ExperienceDbEntity>>?

    @Insert
    suspend fun insertProgress(progressDbEntity: ProgressDbEntity): Long

    @Insert
    fun insertUserHobby(userHobbyDbEntity: UserHobbyDbEntity)

    @Insert
    fun insertExperience(experienceDbEntity: ExperienceDbEntity)

    @Update(entity = ExperienceDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateExperience(experience: ExperienceDbEntity)

    @Update(entity = ProgressDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateProgress(progressDbEntity: ProgressDbEntity)

    @Query("SELECT progress_id FROM user_hobbies WHERE user_hobbies.id = :uhId")
    fun findProgressIdByUserHobbyId(uhId: Int): Int

    @Delete(entity = UserHobbyDbEntity::class)
    fun deleteUserHobby(userHobbyDbEntity: UserHobbyDbEntity)

    @Query("SELECT EXISTS (SELECT * FROM user_hobbies WHERE user_hobbies.hobby_id = :hobbyId)")
    fun userHobbyExists(hobbyId: Int): Boolean

    @Delete(entity = UserHobbyDbEntity::class)
    fun deleteUserHobbies(map: List<UserHobbyDbEntity>)

    @Query("SELECT * FROM experiences WHERE experiences.id = :experienceId")
    fun getUserExperienceById(experienceId: Int): Flow<ExperienceDbEntity>

    @Query("UPDATE experiences SET note = :noteText WHERE id = :experienceId")
    fun updateNoteTextByExperienceId(noteText: String, experienceId: Int)

    @Insert
    fun insertUriReferences(imageReferences: List<ImageReferenceDbEntity>)

    @Query("SELECT * FROM image_references WHERE image_references.experience_id = :experienceId")
    fun findImageReferencesByExperienceId(experienceId: Int): Flow<List<ImageReferenceDbEntity>>
}
