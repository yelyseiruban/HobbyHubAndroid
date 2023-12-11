package com.yelysei.hobbyharbor.model.userhobbies

import android.database.Cursor
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import androidx.core.content.contentValuesOf
import androidx.core.database.sqlite.transaction
import com.yelysei.hobbyharbor.model.NoUserHobbiesFoundException
import com.yelysei.hobbyharbor.model.UserHobbyDoesNotExistException
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.sqlite.AppSQLiteContract.ActionsTable
import com.yelysei.hobbyharbor.model.sqlite.AppSQLiteContract.HobbiesTable
import com.yelysei.hobbyharbor.model.sqlite.AppSQLiteContract.ProgressesTable
import com.yelysei.hobbyharbor.model.sqlite.AppSQLiteContract.UserHobbiesTable
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.Progress
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext


class SQLiteUserHobbiesRepository(
    private val db: SQLiteDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : UserHobbiesRepository {

    private val currentUserHobbiesFlow = MutableSharedFlow<List<UserHobby>>(replay = 1)

    override suspend fun getUserHobbyById(id: Int): Flow<UserHobby> {
        TODO("Not yet implemented")
    }

    override suspend fun addUserHobby(userHobby: UserHobby) = withContext(ioDispatcher) {
        insertUserHobby(userHobby.hobby.id, userHobby.progress.goal)
        val updatedUserHobbies = currentUserHobbiesFlow.replayCache.first().toMutableList()
        updatedUserHobbies.add(userHobby)
        currentUserHobbiesFlow.emit(updatedUserHobbies)
    }

    override suspend fun addUserHobbyExperience(uhId: Int, action: Action) = withContext(ioDispatcher) {
        insertUserHobbyExperience(uhId, action)

        val updatedList = currentUserHobbiesFlow.replayCache.first().toMutableList()
        val updatedUserHobby = updatedList.find { it.id == uhId }
        if (updatedUserHobby != null) {
            updatedUserHobby.progress.history += action
            currentUserHobbiesFlow.emit(updatedList)
        } else {
            throw UserHobbyDoesNotExistException()
        }

    }

    override suspend fun getUserHobbies(): Flow<List<UserHobby>> = withContext(ioDispatcher) {
        currentUserHobbiesFlow.tryEmit(selectUserHobbies())
        return@withContext currentUserHobbiesFlow
    }

    private fun selectUserHobbies(): List<UserHobby> {
        val cursor = db.rawQuery(
            "SELECT\n" +
                "    user_hobbies.id=?,\n" +
                "    user_hobbies.hobby_id=?,\n" +
                "    hobbies.hobby_name=?,\n" +
                "    hobbies.category_name=?,\n" +
                "    hobbies.cost=?,\n" +
                "    hobbies.place=?,\n" +
                "    hobbies.people=?,\n" +
                "\tprogresses.id=? AS progress_id,\n" +
                "    progresses.goal=?\n" +
                "FROM\n" +
                "    user_hobbies\n" +
                "JOIN\n" +
                "    hobbies ON user_hobbies.hobby_id = hobbies.id\n" +
                "JOIN\n" +
                "    progresses ON user_hobbies.progress_id = progresses.id;",
            arrayOf(
                UserHobbiesTable.COLUMN_ID,
                "hobby_id",
                HobbiesTable.COLUMN_HOBBY_NAME,
                HobbiesTable.COLUMN_CATEGORY_NAME,
                HobbiesTable.COLUMN_COST,
                HobbiesTable.COLUMN_PLACE,
                HobbiesTable.COLUMN_PEOPLE,
                "progress_id",
                ProgressesTable.COLUMN_GOAL
            )
        )
        val userHobbies = mutableListOf<UserHobby>()
        return cursor.use {
            if(it.count == 0) throw NoUserHobbiesFoundException()
            if (cursor.moveToFirst()) {
                do {
                    userHobbies += parseUserHobby(it)
                } while (cursor.moveToNext())
            }
            userHobbies
        }
    }

    private fun parseUserHobby(cursor: Cursor): UserHobby {
        val userHobbyId: Int = cursor.getInt(cursor.getColumnIndexOrThrow(UserHobbiesTable.COLUMN_ID))
        val hobbyId: Int = cursor.getInt(cursor.getColumnIndexOrThrow("hobby_id"))
        val hobbyName: String = cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_HOBBY_NAME))
        val categoryName: String = cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_CATEGORY_NAME))
        val cost: String = cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_COST))
        val place: String = cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_PLACE))
        val people: String = cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_PEOPLE))
        val progressId: Int = cursor.getInt(cursor.getColumnIndexOrThrow("progress_id"))
        val goal: Int = cursor.getInt(cursor.getColumnIndexOrThrow(ProgressesTable.COLUMN_GOAL))
        val actions = getActionsByProgressId(progressId)
        val hobby = Hobby(hobbyId, hobbyName, categoryName, cost, place, people);
        val progress = Progress(progressId, actions, goal)
        return UserHobby(
            userHobbyId,
            hobby,
            progress
        )
    }

    private fun getActionsByProgressId(progressId: Int): MutableList<Action> {
        val cursor = db.rawQuery(
            "SELECT\n" +
                "    actions.id AS action_id,\n" +
                "    actions.start_date,\n" +
                "    actions.end_date\n" +
                "FROM\n" +
                "    actions\n" +
                "JOIN\n" +
                "    progresses ON actions.progress_id = progresses.id\n" +
                "WHERE\n" +
                "    actions.progress_id = $progressId;\n",
            arrayOf(ActionsTable.COLUMN_ID, ActionsTable.COLUMN_START_DATE, ActionsTable.COLUMN_END_DATE)
        )
        val actions = mutableListOf<Action>()
        return cursor.use {
            if (cursor.moveToFirst()) {
                do {
                    actions += Action(
                        it.getInt(cursor.getColumnIndexOrThrow(ActionsTable.COLUMN_ID)),
                        it.getInt(cursor.getColumnIndexOrThrow(ActionsTable.COLUMN_START_DATE)).toLong(),
                        it.getInt(cursor.getColumnIndexOrThrow(ActionsTable.COLUMN_END_DATE)).toLong()
                    )
                } while (cursor.moveToNext())
            }
            actions
        }
    }

    fun insertUserHobby(hobbyId: Int, goal: Int) {
        try {
            db.transaction {
                db.insertOrThrow(
                    ProgressesTable.TABLE_NAME,
                    null,
                    contentValuesOf(
                        ProgressesTable.COLUMN_GOAL to goal
                    )
                )
                val progressesCursor = db.rawQuery("SELECT * FROM ${ProgressesTable.TABLE_NAME};", arrayOf())
                val progressId = progressesCursor.count
                progressesCursor.close()
                db.insertOrThrow(
                    UserHobbiesTable.TABLE_NAME,
                    null,
                    contentValuesOf(
                        UserHobbiesTable.COLUMN_HOBBY_ID to hobbyId,
                        UserHobbiesTable.COLUMN_PROGRESS_ID to progressId
                    )
                )
            }
        } catch (e: SQLiteConstraintException) {
            throw e
        }
    }

    fun insertUserHobbyExperience(uhId: Int, action: Action) {
        val progressIdCursor = db.rawQuery(
            "SELECT progresses.id\n" +
            "FROM progresses\n" +
            "JOIN user_hobbies ON user_hobbies.progress_id = progresses.id\n" +
            "WHERE user_hobbies.id = $uhId;",
            arrayOf(ProgressesTable.COLUMN_ID)
        )
        progressIdCursor.moveToFirst()
        val progressId = progressIdCursor.use {
            it.getInt(it.getColumnIndexOrThrow(ProgressesTable.COLUMN_ID))
        }
        db.insertOrThrow(
            ActionsTable.TABLE_NAME,
            null,
            contentValuesOf(
                ActionsTable.COLUMN_START_DATE to action.startTime,
                ActionsTable.COLUMN_END_DATE to action.endTime,
                ActionsTable.COLUMN_PROGRESS_ID to progressId
            )
        )
    }
}
