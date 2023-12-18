package com.yelysei.hobbyharbor.model.userhobbies

import android.database.sqlite.SQLiteConstraintException
import android.util.Log
import com.yelysei.hobbyharbor.Repositories.userHobbiesRepository
import com.yelysei.hobbyharbor.model.NoActionsByProgressIdException
import com.yelysei.hobbyharbor.model.NoHobbyIdException
import com.yelysei.hobbyharbor.model.UserHobbyAlreadyAddedException
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.Progress
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.room.UserHobbiesDao
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ActionDbEntity
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
                    userHobbiesRepository.getActionsByProgressId(progressId),
                    userHobbiesRepository.getProgressById(progressId)
                ) { actions, progress ->
                    // Combine the results
                    userHobby.progress = progress
                    userHobby.progress.actions = actions
                    userHobby
                }
            }
    }



    override suspend fun addUserHobby(hobby: Hobby, goal: Int) = withContext(ioDispatcher) {
        val progressId = userHobbiesDao.insertProgress(ProgressDbEntity(id = 0, goal)).toInt()
        try {
            userHobbiesDao.insertUserHobby(UserHobbyDbEntity(id = 0, hobbyId = hobby.id ?: throw NoHobbyIdException(), progressId = progressId))
        } catch (e: SQLiteConstraintException) {
            throw UserHobbyAlreadyAddedException()
        }

    }

    override suspend fun addUserHobbyExperience(progressId: Int,  action: Action) = withContext(ioDispatcher) {
        userHobbiesDao.insertUserHobbyAction(ActionDbEntity.fromAction(action, progressId))
    }

    override suspend fun updateUserHobbyExperience(progressId: Int, action: Action) = withContext(ioDispatcher) {
        userHobbiesDao.updateUserHobbyAction(ActionDbEntity.fromAction(action, progressId))
    }

    override suspend fun updateProgress(progressDbEntity: ProgressDbEntity): Unit = withContext(ioDispatcher) {
        Log.d("Repository", "Updating progress: $progressDbEntity")
        userHobbiesDao.updateProgress(progressDbEntity)
        Log.d("Repository", "Progress updated successfully.")
    }

    override suspend fun getUserHobbies(): Flow<List<UserHobby>> = withContext(ioDispatcher) {
        val userHobbiesFlow = userHobbiesDao.getUserHobbies().map {
            it.map {userHobbiesInTuple ->
                val userHobby = userHobbiesInTuple.toUserHobby()
                val actions = getActionsByProgressId(userHobby.progress.id)
                userHobby.progress.actions = actions.first()
                return@map userHobby
            }
        }
        return@withContext userHobbiesFlow
    }

    override suspend fun getProgressById(id: Int): Flow<Progress> = withContext(ioDispatcher) {
        return@withContext userHobbiesDao.findProgressById(id).map {
            it.toProgress()
        }
    }

    override suspend fun getActionsByProgressId(progressId: Int): Flow<List<Action>> {
        val actionsDbEntityFlow = userHobbiesDao.findUserActionsByProgressId(progressId)
        val actions = actionsDbEntityFlow?.map {actionsDbEntity ->
            actionsDbEntity.map {
                it.toAction()
            }
        } ?: throw NoActionsByProgressIdException()
        return actions
    }
}
