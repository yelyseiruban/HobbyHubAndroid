package com.yelysei.hobbyharbor.model.hobbies

import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import kotlinx.coroutines.flow.Flow

interface HobbiesRepository  {
    suspend fun getCurrentHobbies(): Flow<List<Hobby>>

    suspend fun getCurrentCategories(): Flow<List<String>>

    suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby>

    fun addCustomHobby(hobby: Hobby)

}