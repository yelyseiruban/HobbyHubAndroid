package com.yelysei.hobbyhub.model.hobbies.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.yelysei.hobbyhub.model.hobbies.room.entities.HobbyDbEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HobbiesDao {

    @Query("SELECT * FROM hobbies WHERE category_name = :categoryName")
    suspend fun findHobbiesByCategoryName(categoryName: String): List<HobbyDbEntity>?

    @Query("SELECT * FROM hobbies WHERE hobby_name LIKE '%' || :searchInput || '%' COLLATE NOCASE")
    suspend fun findHobbiesByHobbyName(searchInput: String): List<HobbyDbEntity>?

    @Query("SELECT * FROM hobbies")
    fun getHobbies(): Flow<List<HobbyDbEntity>>

    @Query("SELECT DISTINCT category_name FROM hobbies")
    fun getCategories(): Flow<List<String>>

    @Insert(entity = HobbyDbEntity::class)
    suspend fun insertCustomHobby(hobby: HobbyDbEntity): Long

    @Query("SELECT EXISTS(SELECT * FROM hobbies WHERE hobby_name = :hobbyName)")
    suspend fun hobbyExists(hobbyName: String): Boolean

}