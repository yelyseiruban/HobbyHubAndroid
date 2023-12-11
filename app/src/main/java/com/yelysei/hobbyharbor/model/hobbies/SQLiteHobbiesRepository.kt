package com.yelysei.hobbyharbor.model.hobbies

import android.content.ContentValues
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.sqlite.AppSQLiteContract.HobbiesTable
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext

class SQLiteHobbiesRepository(
    private val db: SQLiteDatabase,
    private val ioDispatcher: CoroutineDispatcher,
    private val defaultDispatcher: CoroutineDispatcher
) : HobbiesRepository {

    val currentHobbiesFlow = MutableSharedFlow<List<Hobby>>(replay = 1)
    val currentCategoriesFlow = MutableSharedFlow<List<String>>(replay = 1)
    var wereSelected = false

    override suspend fun getCurrentHobbies(): Flow<List<Hobby>> = withContext(ioDispatcher) {
        if (wereSelected) return@withContext currentHobbiesFlow
        currentHobbiesFlow.tryEmit(selectAvailableHobbies())
        return@withContext currentHobbiesFlow
    }

    override suspend fun getCurrentCategories(): Flow<List<String>> = withContext(ioDispatcher){
        if (!wereSelected) currentHobbiesFlow.tryEmit(selectAvailableHobbies())

        val categories = mutableSetOf<String>()
        val availableHobbies = currentHobbiesFlow.replayCache.first()
        availableHobbies.forEach { hobby: Hobby -> categories.add(hobby.categoryName) }
        currentCategoriesFlow.tryEmit(categories.toList())
        return@withContext currentCategoriesFlow
    }

    override suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby> = withContext(defaultDispatcher) {
        if (!wereSelected) currentHobbiesFlow.tryEmit(selectAvailableHobbies())
        val availableHobbies = currentHobbiesFlow.replayCache.first()
        return@withContext availableHobbies.filter { hobby: Hobby -> hobby.categoryName == categoryName }
    }

    override fun addCustomHobby(hobby: Hobby) {
        val hobbyId = insertCustomHobby(hobby)
        val updatedHobbies = currentHobbiesFlow.replayCache.first().toMutableList()
        hobby.id = hobbyId
        updatedHobbies += hobby
        currentHobbiesFlow.tryEmit(updatedHobbies)
        val currentCategories = currentCategoriesFlow.replayCache.first().toMutableList()
        if (currentCategories.contains(hobby.categoryName)) return
        currentCategories += hobby.categoryName
        currentCategoriesFlow.tryEmit(currentCategories)
    }

    private fun selectAvailableHobbies() : List<Hobby> {
        val cursor = db.rawQuery("SELECT * FROM ${HobbiesTable.TABLE_NAME};",
            arrayOf(
                HobbiesTable.COLUMN_ID,
                HobbiesTable.COLUMN_HOBBY_NAME,
                HobbiesTable.COLUMN_CATEGORY_NAME,
                HobbiesTable.COLUMN_COST,
                HobbiesTable.COLUMN_PLACE,
                HobbiesTable.COLUMN_PEOPLE
            )
        )
        val hobbies: MutableList<Hobby> = mutableListOf()
        cursor.use {
            while (it.moveToNext()) {
                hobbies += parseHobby(it)
            }
        }
        return hobbies
    }

    private fun parseHobby(cursor: Cursor) : Hobby {
        return Hobby(
            cursor.getInt(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_ID)),
            cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_HOBBY_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_CATEGORY_NAME)),
            cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_COST)),
            cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_PLACE)),
            cursor.getString(cursor.getColumnIndexOrThrow(HobbiesTable.COLUMN_PEOPLE))
        )
    }

    /**
     * Inserts hobby into hobbies table and returns its id
     */
    private fun insertCustomHobby(hobby: Hobby): Int {
        val values = ContentValues()
        values.put(HobbiesTable.COLUMN_HOBBY_NAME, hobby.hobbyName)
        values.put(HobbiesTable.COLUMN_CATEGORY_NAME, hobby.categoryName)
        values.put(HobbiesTable.COLUMN_COST, hobby.cost)
        values.put(HobbiesTable.COLUMN_PLACE, hobby.place)
        values.put(HobbiesTable.COLUMN_PEOPLE, hobby.people)

        val id = db.insert(HobbiesTable.TABLE_NAME, null, values)
        return id.toInt()
    }

}