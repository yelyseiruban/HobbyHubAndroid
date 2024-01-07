package com.yelysei.hobbyharbor.model.userhobbies

import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Experience
import com.yelysei.hobbyharbor.model.userhobbies.entities.ImageReference
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import kotlinx.coroutines.flow.Flow

interface UserHobbiesRepository {
    suspend fun getUserHobbyById(id: Int): Flow<UserHobby>

    suspend fun getUserHobbies(): Flow<List<UserHobby>>

    /**
     * @throws UserHobbyAlreadyAddedException
     */
    suspend fun addUserHobby(hobby: Hobby, goal: Int)

    suspend fun addUserHobbyExperience(progressId: Int, experience: Experience)

    suspend fun updateUserHobbyExperience(progressId: Int, experience: Experience)

    suspend fun updateProgress(progressDbEntity: ProgressDbEntity)

    suspend fun deleteUserHobby(userHobby: UserHobby)

    suspend fun deleteUserHobbies(userHobbies: List<UserHobby>)

    suspend fun userHobbyExists(hobbyId: Int): Boolean
    suspend fun getUserExperienceById(experienceId: Int): Flow<Experience>

    suspend fun updateNoteTextByExperienceId(noteText: String, experienceId: Int)

    suspend fun insertUriReferences(uriReferences: List<String>, experienceId: Int)

    suspend fun getUriReferencesByExperienceId(experienceId: Int): Flow<List<ImageReference>>

}