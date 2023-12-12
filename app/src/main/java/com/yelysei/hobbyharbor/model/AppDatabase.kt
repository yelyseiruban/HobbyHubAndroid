package com.yelysei.hobbyharbor.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.yelysei.hobbyharbor.model.hobbies.room.HobbiesDao
import com.yelysei.hobbyharbor.model.hobbies.room.entities.HobbyDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.UserHobbiesDao
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ActionDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyharbor.model.userhobbies.room.entities.UserHobbyDbEntity

@Database(
    version = 1,
    entities = [
        HobbyDbEntity::class,
        UserHobbyDbEntity::class,
        ActionDbEntity::class,
        ProgressDbEntity::class
    ]
)
abstract class AppDatabase : RoomDatabase(){

    abstract fun getHobbiesDao(): HobbiesDao

    abstract fun getUserHobbiesDao(): UserHobbiesDao

}