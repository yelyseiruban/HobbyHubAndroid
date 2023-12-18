package com.yelysei.hobbyharbor.model.userhobbies

import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.Progress
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import kotlinx.coroutines.flow.Flow

interface UserHobbiesRepository  {
    suspend fun getUserHobbyById(id: Int): Flow<UserHobby>

    suspend fun getUserHobbies(): Flow<List<UserHobby>>

    suspend fun getProgressById(id: Int): Flow<Progress>

    /**
     * @throws UserHobbyAlreadyAddedException
     */
    suspend fun addUserHobby(hobby: Hobby, goal: Int)

    suspend fun addUserHobbyExperience(progressId: Int, action: Action)

    suspend fun updateUserHobbyExperience(progressId: Int, action: Action)

    suspend fun updateProgress(progressDbEntity: ProgressDbEntity)


    suspend fun getActionsByProgressId(progressId: Int): Flow<List<Action>>
}