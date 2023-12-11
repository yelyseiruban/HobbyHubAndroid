package com.yelysei.hobbyharbor.model.userhobbies

import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import kotlinx.coroutines.flow.Flow

interface UserHobbiesRepository  {
    suspend fun getUserHobbyById(id: Int): Flow<UserHobby>

    suspend fun getUserHobbies(): Flow<List<UserHobby>>

    suspend fun addUserHobby(userHobby: UserHobby)

    suspend fun addUserHobbyExperience(uhId: Int, action: Action)

}