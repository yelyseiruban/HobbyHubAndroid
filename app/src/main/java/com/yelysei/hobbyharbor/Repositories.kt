package com.yelysei.hobbyharbor

import android.content.Context
import androidx.room.Room
import com.yelysei.hobbyharbor.model.AppDatabase
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.RoomHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.RoomUserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import kotlinx.coroutines.Dispatchers

object Repositories {
    private lateinit var applicationContext: Context

    private val database: AppDatabase by lazy<AppDatabase> {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .createFromAsset("initial_database.db")
            .build()
    }

    val hobbiesRepository: HobbiesRepository by lazy {
        RoomHobbiesRepository(database.getHobbiesDao(), Dispatchers.IO, Dispatchers.Default)
    }
    val userHobbiesRepository: UserHobbiesRepository by lazy {
        RoomUserHobbiesRepository(database.getUserHobbiesDao(), Dispatchers.IO, Dispatchers.Default)
    }

    fun init(context: Context) {
        applicationContext = context
    }
}