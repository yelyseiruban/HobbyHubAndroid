package com.yelysei.hobbyharbor

import android.content.Context
import androidx.room.Room
import com.yelysei.hobbyharbor.model.AppDatabase
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.RoomHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.RoomUserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi

object Repositories {
    private lateinit var applicationContext: Context

    private val database: AppDatabase by lazy {
        Room.databaseBuilder(applicationContext, AppDatabase::class.java, "database.db")
            .createFromAsset("initial_database.db")
//            .addMigrations(AppDatabase.MIGRATION_1_2)
            .build()
    }

    val hobbiesRepository: HobbiesRepository by lazy {
        RoomHobbiesRepository(database.getHobbiesDao(), Dispatchers.IO, Dispatchers.Default)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userHobbiesRepository: UserHobbiesRepository by lazy {
        RoomUserHobbiesRepository(database.getUserHobbiesDao(), Dispatchers.IO)
    }

    fun init(context: Context) {
        applicationContext = context
    }
}