package com.yelysei.hobbyhub.model.hobbies

import android.database.sqlite.SQLiteConstraintException
import com.yelysei.hobbyhub.model.HobbyAlreadyExistsException
import com.yelysei.hobbyhub.model.NoHobbiesBySpecifiedCategoryName
import com.yelysei.hobbyhub.model.hobbies.entities.Hobby
import com.yelysei.hobbyhub.model.hobbies.room.HobbiesDao
import com.yelysei.hobbyhub.model.hobbies.room.entities.HobbyDbEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomHobbiesRepository(
    private val hobbiesDao: HobbiesDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : HobbiesRepository {

    override val currentHobbiesFlow: Flow<List<Hobby>>
    override val currentCategoriesFlow: Flow<List<String>>

    init {
        currentHobbiesFlow = getCurrentHobbies()
        currentCategoriesFlow = getCurrentCategories()
    }

    override fun getCurrentHobbies(): Flow<List<Hobby>> {
        return hobbiesDao.getHobbies().map {
            it.map { hobbyDbEntity -> hobbyDbEntity.toHobby() }
        }
    }

    override fun getCurrentCategories(): Flow<List<String>> {
        return hobbiesDao.getCategories()
    }

    /**
     * implemented
     */
    override suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby> =
        withContext(defaultDispatcher) {
            return@withContext hobbiesDao.findHobbiesByCategoryName(categoryName)?.map {
                it.toHobby()
            } ?: throw NoHobbiesBySpecifiedCategoryName()
        }

    override suspend fun addCustomHobby(hobby: Hobby): Int = withContext(ioDispatcher) {
        try {
            hobbiesDao.insertCustomHobby(HobbyDbEntity.fromHobby(hobby)).toInt()
        } catch (e: SQLiteConstraintException) {
            throw HobbyAlreadyExistsException(hobby.hobbyName)
        }
    }

    override suspend fun getHobbiesByHobbyName(hobbyNameSearchInput: String): List<Hobby> =
        withContext(ioDispatcher) {
            return@withContext hobbiesDao.findHobbiesByHobbyName(hobbyNameSearchInput)?.map {
                it.toHobby()
            } ?: emptyList()
        }

    override suspend fun hobbyExists(hobbyName: String): Boolean = withContext(ioDispatcher) {
        hobbiesDao.hobbyExists(hobbyName)
    }

}