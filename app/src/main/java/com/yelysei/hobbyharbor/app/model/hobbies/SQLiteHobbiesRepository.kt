package com.yelysei.hobbyharbor.app.model.hobbies

import android.database.sqlite.SQLiteDatabase
import com.yelysei.hobbyharbor.app.model.coroutines.IoDispatcher
import com.yelysei.hobbyharbor.app.model.coroutines.WorkerDispatcher
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class SQLiteHobbiesRepository(
    private val database: SQLiteDatabase,
    private val ioDispatcher: IoDispatcher,
    private val workerDispatcher: WorkerDispatcher
) : HobbiesRepository {
    private val currentHobbiesFlow = MutableSharedFlow<List<Hobby>>(
        replay = 0,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun listenCurrentHobbies(): Flow<List<Hobby>> = currentHobbiesFlow

    override suspend fun getAvailableHobbies(): List<Hobby> = withContext(ioDispatcher.value) {
        delay(1000)
        return@withContext AVAILABLE_HOBBIES
    }

    override suspend fun getAvailableCategories(): List<String> = withContext(workerDispatcher.value){
        delay(1000)
        val categories = mutableSetOf<String>()
        AVAILABLE_HOBBIES.forEach { hobby: Hobby ->  categories.add(hobby.categoryName) }
        return@withContext categories.toList()
    }

    override suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby> = withContext(workerDispatcher.value) {
        delay(1000)
        return@withContext AVAILABLE_HOBBIES.filter { hobby: Hobby -> hobby.categoryName == categoryName }
    }

    override fun addHobby(hobby: Hobby): Flow<Int> = flow {
        var progress = 0
        while (progress < 100) {
            progress += 2
            delay(30)
            emit(progress)
        }
        AVAILABLE_HOBBIES += hobby
        currentHobbiesFlow.emit(AVAILABLE_HOBBIES)
    }.flowOn(ioDispatcher.value)

    companion object {
        private val AVAILABLE_HOBBIES = mutableListOf(
            (Hobby(1, "guitar", "music", "low", "anywhere", "whatever")),
            (Hobby(2, "piano", "music", "low", "anywhere", "whatever")),
            (Hobby(3, "bass", "music", "low", "anywhere", "whatever")),
            (Hobby(4, "walk", "outdoor", "low", "anywhere", "whatever")),
            (Hobby(5, "mock", "mock1", "low", "anywhere", "whatever")),
            (Hobby(6, "mock", "mock2", "low", "anywhere", "whatever")),
            (Hobby(7, "mock", "mock3", "low", "anywhere", "whatever")),
        )

    }
}