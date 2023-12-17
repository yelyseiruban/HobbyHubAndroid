package com.yelysei.hobbyharbor.model.userhobbies.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ActionDbEntity
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
                "ON user_hobbies.progress_id = progresses.id")
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

    @Insert
    suspend fun addUserHobby(userHobby: UserHobbyDbEntity)

    @Query("SELECT * FROM actions WHERE actions.progress_id = :progressId")
    fun findUserActionsByProgressId(progressId: Int): Flow<List<ActionDbEntity>>?

    @Insert
    suspend fun insertProgress(progressDbEntity: ProgressDbEntity): Long
    @Insert
    fun insertUserHobby(userHobbyDbEntity: UserHobbyDbEntity)
    @Insert
    fun insertUserHobbyAction(actionDbEntity: ActionDbEntity)
    @Update(entity = ActionDbEntity::class, onConflict = OnConflictStrategy.REPLACE)
    fun updateUserHobbyAction(fromAction: ActionDbEntity)

    @Query("SELECT progress_id FROM user_hobbies WHERE user_hobbies.id = :uhId")
    fun findProgressIdByUserHobbyId(uhId: Int): Int

}
