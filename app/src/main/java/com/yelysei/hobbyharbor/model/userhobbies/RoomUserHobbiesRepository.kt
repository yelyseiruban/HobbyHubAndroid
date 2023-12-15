package com.yelysei.hobbyharbor.model.userhobbies

import android.database.sqlite.SQLiteConstraintException
import com.yelysei.hobbyharbor.model.NoActionsByProgressIdException
import com.yelysei.hobbyharbor.model.NoHobbyIdException
import com.yelysei.hobbyharbor.model.UserHobbyAlreadyAddedException
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.room.UserHobbiesDao
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ActionDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.UserHobbyDbEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class RoomUserHobbiesRepository(
    private val userHobbiesDao: UserHobbiesDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : UserHobbiesRepository {

    override suspend fun getUserHobbyById(id: Int): Flow<UserHobby> {
        val userHobbyFlow = userHobbiesDao.findUserHobbyById(id).map {
            it.toUserHobby()
        }
        return userHobbyFlow
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
