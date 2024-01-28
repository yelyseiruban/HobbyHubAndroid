package com.yelysei.hobbyhub.model.userhobbies

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.yelysei.hobbyhub.model.NoExperiencesByProgressIdException
import com.yelysei.hobbyhub.model.NoHobbyIdException
import com.yelysei.hobbyhub.model.UserHobbyAlreadyAddedException
import com.yelysei.hobbyhub.model.hobbies.entities.Hobby
import com.yelysei.hobbyhub.model.userhobbies.entities.Experience
import com.yelysei.hobbyhub.model.userhobbies.entities.ImageReference
import com.yelysei.hobbyhub.model.userhobbies.entities.Progress
import com.yelysei.hobbyhub.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyhub.model.userhobbies.room.UserHobbiesDao
import com.yelysei.hobbyhub.model.userhobbies.room.entities.ExperienceDbEntity
import com.yelysei.hobbyhub.model.userhobbies.room.entities.ImageReferenceDbEntity
import com.yelysei.hobbyhub.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyhub.model.userhobbies.room.entities.UserHobbyDbEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


@kotlinx.coroutines.ExperimentalCoroutinesApi
class RoomUserHobbiesRepository(
    private val userHobbiesDao: UserHobbiesDao
) : UserHobbiesRepository {

    override suspend fun getUserHobbyById(id: Int): Flow<UserHobby> = withContext(Dispatchers.IO) {
        userHobbiesDao.findUserHobbyById(id)
            .flatMapLatest { userHobbyDbEntity ->
                val userHobby = userHobbyDbEntity.toUserHobby()
                val progressId = userHobby.progress.id

                combine(
                    getExperiencesByProgressId(progressId),
                    getProgressById(progressId)
                ) { experiences, progress ->
                    // Combine the results
                    userHobby.progress = progress
                    userHobby.progress.experiences = experiences
                    userHobby
                }
            }
    }

    override suspend fun addUserHobby(hobby: Hobby, goal: Int) = withContext(Dispatchers.IO) {
        val progressId = userHobbiesDao.insertProgress(ProgressDbEntity(id = 0, goal)).toInt()
        try {
            userHobbiesDao.insertUserHobby(
                UserHobbyDbEntity(
                    id = 0,
                    hobbyId = hobby.id ?: throw NoHobbyIdException(),
                    progressId = progressId
                )
            )
        } catch (e: SQLiteConstraintException) {
            throw UserHobbyAlreadyAddedException()
        }
    }

    override suspend fun addUserHobbyExperience(progressId: Int, experience: Experience) =
        withContext(Dispatchers.IO) {
            userHobbiesDao.insertExperience(
                ExperienceDbEntity.fromExperience(
                    experience,
                    progressId
                )
            )
        }

    override suspend fun updateUserHobbyExperience(progressId: Int, experience: Experience) =
        withContext(Dispatchers.IO) {
            userHobbiesDao.updateExperience(
                ExperienceDbEntity.fromExperience(
                    experience,
                    progressId
                )
            )
        }

    override suspend fun updateProgress(progressDbEntity: ProgressDbEntity): Unit =
        withContext(Dispatchers.IO) {
            Log.d("Repository", "Updating progress: $progressDbEntity")
            userHobbiesDao.updateProgress(progressDbEntity)
            Log.d("Repository", "Progress updated successfully.")
        }

    override suspend fun deleteUserHobby(userHobby: UserHobby) = withContext(Dispatchers.IO) {
        userHobbiesDao.deleteUserHobby(UserHobbyDbEntity.fromUserHobby(userHobby))
    }

    override suspend fun deleteUserHobbies(userHobbies: List<UserHobby>) =
        withContext(Dispatchers.IO) {
            userHobbiesDao.deleteUserHobbies(userHobbies.map { UserHobbyDbEntity.fromUserHobby(it) })
        }

    override suspend fun userHobbyExists(hobbyId: Int) = withContext(Dispatchers.IO) {
        userHobbiesDao.userHobbyExists(hobbyId)
    }

    override suspend fun getUserExperienceById(experienceId: Int): Flow<Experience> =
        withContext(Dispatchers.IO) {
            userHobbiesDao.getUserExperienceById(experienceId).map {
                it.toExperience()
            }
        }

    override suspend fun updateNoteTextByExperienceId(noteText: String, experienceId: Int) =
        withContext(Dispatchers.IO) {
            userHobbiesDao.updateNoteTextByExperienceId(noteText, experienceId)
        }

    override suspend fun insertUriReferences(uriReferences: List<String>, experienceId: Int) =
        withContext(Dispatchers.IO) {
            val imageReferences = uriReferences.map {
                ImageReferenceDbEntity(
                    id = 0,
                    it,
                    experienceId
                )
            }
            userHobbiesDao.insertUriReferences(imageReferences)
        }

    override suspend fun getUriReferencesByExperienceId(experienceId: Int): Flow<List<ImageReference>> =
        withContext(Dispatchers.IO) {
            userHobbiesDao.findImageReferencesByExperienceId(experienceId)
                .map { imageReferenceDbEntities ->
                    imageReferenceDbEntities.map {
                        it.toImageReference()
                    }
                }
        }

    override suspend fun deleteImageReferences(imageReferences: List<ImageReference>) =
        withContext(Dispatchers.IO) {
            userHobbiesDao.deleteImageReferences(imageReferences.map {
                ImageReferenceDbEntity.fromImageReference(
                    it
                )
            })
        }

    override suspend fun getUserHobbies(): Flow<List<UserHobby>> = withContext(Dispatchers.IO) {
        val userHobbiesFlow = userHobbiesDao.getUserHobbies().map {
            it.map { userHobbiesInTuple ->
                val userHobby = userHobbiesInTuple.toUserHobby()
                val experiences = getExperiencesByProgressId(userHobby.progress.id)
                userHobby.progress.experiences = experiences.first()
                userHobby
            }
        }
        return@withContext userHobbiesFlow
    }

    private suspend fun getProgressById(id: Int): Flow<Progress> = withContext(Dispatchers.IO) {
        return@withContext userHobbiesDao.findProgressById(id).map {
            it.toProgress()
        }
    }

    private suspend fun getExperiencesByProgressId(progressId: Int): Flow<List<Experience>> =
        withContext(Dispatchers.IO) {
            val experiencesDbEntityFlow = userHobbiesDao.findExperiencesByProgressId(progressId)
            return@withContext experiencesDbEntityFlow?.map { experienceDbEntity ->
                experienceDbEntity.map {
                    it.toExperience()
                }
            } ?: throw NoExperiencesByProgressIdException()
        }
}
