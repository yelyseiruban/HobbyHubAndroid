package com.yelysei.hobbyharbor.model.userhobbies

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.yelysei.hobbyharbor.model.NoExperiencesByProgressIdException
import com.yelysei.hobbyharbor.model.NoHobbyIdException
import com.yelysei.hobbyharbor.model.UserHobbyAlreadyAddedException
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Experience
import com.yelysei.hobbyharbor.model.userhobbies.entities.ImageReference
import com.yelysei.hobbyharbor.model.userhobbies.entities.Progress
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.room.UserHobbiesDao
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ExperienceDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ImageReferenceDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.UserHobbyDbEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


@kotlinx.coroutines.ExperimentalCoroutinesApi
class RoomUserHobbiesRepository(
    private val userHobbiesDao: UserHobbiesDao,
    private val ioDispatcher: CoroutineDispatcher
) : UserHobbiesRepository {

    override suspend fun getUserHobbyById(id: Int): Flow<UserHobby> = withContext(ioDispatcher) {
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

    override suspend fun addUserHobby(hobby: Hobby, goal: Int) = withContext(ioDispatcher) {
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
        withContext(ioDispatcher) {
            userHobbiesDao.insertExperience(ExperienceDbEntity.fromExperience(experience, progressId))
        }

    override suspend fun updateUserHobbyExperience(progressId: Int, experience: Experience) =
        withContext(ioDispatcher) {
            userHobbiesDao.updateExperience(ExperienceDbEntity.fromExperience(experience, progressId))
        }

    override suspend fun updateProgress(progressDbEntity: ProgressDbEntity): Unit =
        withContext(ioDispatcher) {
            Log.d("Repository", "Updating progress: $progressDbEntity")
            userHobbiesDao.updateProgress(progressDbEntity)
            Log.d("Repository", "Progress updated successfully.")
        }

    override suspend fun deleteUserHobby(userHobby: UserHobby) = withContext(ioDispatcher) {
        userHobbiesDao.deleteUserHobby(UserHobbyDbEntity.fromUserHobby(userHobby))
    }

    override suspend fun deleteUserHobbies(userHobbies: List<UserHobby>) =
        withContext(ioDispatcher) {
            userHobbiesDao.deleteUserHobbies(userHobbies.map { UserHobbyDbEntity.fromUserHobby(it) })
        }

    override suspend fun userHobbyExists(hobbyId: Int) = withContext(ioDispatcher) {
        userHobbiesDao.userHobbyExists(hobbyId)
    }

    override suspend fun getUserExperienceById(experienceId: Int): Flow<Experience> = withContext(ioDispatcher) {
        userHobbiesDao.getUserExperienceById(experienceId).map {
            it.toExperience()
        }
    }

    override suspend fun updateNoteTextByExperienceId(noteText: String, experienceId: Int) = withContext(ioDispatcher) {
        userHobbiesDao.updateNoteTextByExperienceId(noteText, experienceId)
    }

    override suspend fun insertUriReferences(uriReferences: List<String>, experienceId: Int) = withContext(ioDispatcher) {
        val imageReferences = uriReferences.map {
            ImageReferenceDbEntity(
                id = 0,
                it,
                experienceId
            )
        }
        userHobbiesDao.insertUriReferences(imageReferences)
    }

    override suspend fun getUriReferencesByExperienceId(experienceId: Int): Flow<List<ImageReference>> = withContext(ioDispatcher) {
        userHobbiesDao.findImageReferencesByExperienceId(experienceId)
            .map { imageReferenceDbEntities ->
                imageReferenceDbEntities.map {
                    it.toImageReference()
                }
            }
    }

    override suspend fun getUserHobbies(): Flow<List<UserHobby>> = withContext(ioDispatcher) {
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

    private suspend fun getProgressById(id: Int): Flow<Progress> = withContext(ioDispatcher) {
        return@withContext userHobbiesDao.findProgressById(id).map {
            it.toProgress()
        }
    }

    private suspend fun getExperiencesByProgressId(progressId: Int): Flow<List<Experience>> =
        withContext(ioDispatcher) {
            val experiencesDbEntityFlow = userHobbiesDao.findExperiencesByProgressId(progressId)
            return@withContext experiencesDbEntityFlow?.map { experienceDbEntity ->
                experienceDbEntity.map {
                    it.toExperience()
                }
            } ?: throw NoExperiencesByProgressIdException()
        }
}
