package com.yelysei.hobbyharbor.app.model.hobbies

import com.yelysei.foundation.model.Repository
import kotlinx.coroutines.flow.Flow

typealias HobbiesListener = (hobbies: List<Hobby>) -> Unit
typealias CategoriesListener = (categories: List<String>) -> Unit
interface HobbiesRepository : Repository {
    suspend fun getAvailableHobbies(): List<Hobby>

    suspend fun getAvailableCategories(): List<String>

    suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby>

    fun addHobby(hobby: Hobby): Flow<Int>

    fun listenCurrentHobbies(): Flow<List<Hobby>>

}