package com.yelysei.hobbyharbor.model.hobbies

import com.yelysei.hobbyharbor.model.NoHobbiesBySpecifiedCategoryName
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.hobbies.room.HobbiesDao
import com.yelysei.hobbyharbor.model.hobbies.room.entities.HobbyDbEntity
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class RoomHobbiesRepository(
    private val hobbiesDao: HobbiesDao,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : HobbiesRepository {

    override suspend fun getCurrentHobbies(): Flow<List<Hobby>> {
        return hobbiesDao.getHobbies().map {
            it.map { hobbyDbEntity -> hobbyDbEntity.toHobby() }
        }
    }

    override suspend fun getCurrentCategories(): Flow<List<String>> {
        return hobbiesDao.getCategories()
    }

    /**
     * implemented
     */
    override suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby> = withContext(defaultDispatcher) {
        return@withContext hobbiesDao.findHobbyByCategoryName(categoryName)?.map {
            it.toHobby()
        } ?: throw NoHobbiesBySpecifiedCategoryName()
    }

    override suspend fun addCustomHobby(hobby: Hobby) = withContext(ioDispatcher) {
        hobbiesDao.insertCustomHobby(HobbyDbEntity.fromHobby(hobby))
    }

}