package com.yelysei.hobbyharbor.model.userhobbies

import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import kotlinx.coroutines.flow.Flow

interface UserHobbiesRepository  {
    suspend fun getUserHobbyById(id: Int): Flow<UserHobby>

    suspend fun getUserHobbies(): Flow<List<UserHobby>>

    suspend fun addUserHobby(hobby: Hobby, goal: Int)

    suspend fun addUserHobbyAction(uhId: Int, action: Action)
}