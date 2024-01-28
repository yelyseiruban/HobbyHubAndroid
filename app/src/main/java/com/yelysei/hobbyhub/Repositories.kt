package com.yelysei.hobbyhub

import android.content.Context
import androidx.room.Room
import com.yelysei.hobbyhub.model.AppDatabase
import com.yelysei.hobbyhub.model.hobbies.HobbiesRepository
import com.yelysei.hobbyhub.model.hobbies.RoomHobbiesRepository
import com.yelysei.hobbyhub.model.userhobbies.RoomUserHobbiesRepository
import com.yelysei.hobbyhub.model.userhobbies.UserHobbiesRepository
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
        RoomHobbiesRepository(database.getHobbiesDao())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    val userHobbiesRepository: UserHobbiesRepository by lazy {
        RoomUserHobbiesRepository(database.getUserHobbiesDao())
    }

    fun init(context: Context) {
        applicationContext = context
    }
}