package com.yelysei.hobbyharbor

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.SQLiteHobbiesRepository
import com.yelysei.hobbyharbor.model.sqlite.AppSQLiteHelper
import com.yelysei.hobbyharbor.model.userhobbies.SQLiteUserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import kotlinx.coroutines.Dispatchers

object Repositories {
    private lateinit var applicationContext: Context

    private val database: SQLiteDatabase by lazy<SQLiteDatabase> {
        AppSQLiteHelper(applicationContext).writableDatabase
    }

    val hobbiesRepository: HobbiesRepository by lazy {
        SQLiteHobbiesRepository(database, Dispatchers.IO, Dispatchers.Default)
    }
    val userHobbiesRepository: UserHobbiesRepository by lazy {
        SQLiteUserHobbiesRepository(database, Dispatchers.IO, Dispatchers.Default)
    }

    fun init(context: Context) {
        applicationContext = context
    }
}