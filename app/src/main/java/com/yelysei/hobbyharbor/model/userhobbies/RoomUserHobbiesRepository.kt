package com.yelysei.hobbyharbor.model.userhobbies

import com.yelysei.hobbyharbor.model.NoActionsByProgressIdException
import com.yelysei.hobbyharbor.model.NoHobbyIdException
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.room.UserHobbiesDao
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ActionDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.UserHobbyDbEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext


class RoomUserHobbiesRepository(
    private val userHobbiesDao: UserHobbiesDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : UserHobbiesRepository {

    override suspend fun getUserHobbyById(id: Int): UserHobby {
        val userHobby = userHobbiesDao.findUserHobbyById(id).toUserHobby()
        userHobby.progress.history = getActionsByProgressId(userHobby.progress.id)
        return userHobby
    }

    override suspend fun addUserHobby(hobby: Hobby, goal: Int) = withContext(ioDispatcher) {
        val progressId = userHobbiesDao.insertProgress(ProgressDbEntity(id = 0, goal)).toInt()
        userHobbiesDao.insertUserHobby(UserHobbyDbEntity(id = 0, hobbyId = hobby.id ?: throw NoHobbyIdException(), progressId = progressId))
    }

    override suspend fun addUserHobbyAction(uhId: Int, action: Action) = withContext(ioDispatcher) {
        val progressId = userHobbiesDao.findProgressIdByUserHobbyId(uhId)
        userHobbiesDao.insertUserHobbyAction(ActionDbEntity.fromAction(action, progressId))
    }

    override suspend fun getUserHobbies(): Flow<List<UserHobby>> = withContext(ioDispatcher) {
        val flow = userHobbiesDao.getUserHobbies().map {
            it.map {userHobbiesInTuple ->
                val userHobby = userHobbiesInTuple.toUserHobby()
                val actions = getActionsByProgressId(userHobby.progress.id)
                userHobby.progress.history = actions
                return@map userHobby
            }
        }
        return@withContext flow
    }

    private suspend fun getActionsByProgressId(progressId: Int): MutableList<Action> {
        val actionsDbEntity = userHobbiesDao.findUserActionsByProgressId(progressId)
        val actions = actionsDbEntity?.map {
            it.toAction()
        } ?: throw NoActionsByProgressIdException()
        return  actions.toMutableList()
    }
}
