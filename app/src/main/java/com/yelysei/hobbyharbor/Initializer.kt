package com.yelysei.hobbyharbor

import android.database.sqlite.SQLiteDatabase
import com.yelysei.foundation.SingletonScopeDependencies
import com.yelysei.hobbyharbor.app.model.AppSQLiteHelper
import com.yelysei.hobbyharbor.app.model.coroutines.IoDispatcher
import com.yelysei.hobbyharbor.app.model.coroutines.WorkerDispatcher
import com.yelysei.hobbyharbor.app.model.hobbies.SQLiteHobbiesRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.SQLiteUserHobbiesRepository

object Initializer {

    fun initDependencies() = SingletonScopeDependencies.init { applicationContext ->

        val ioDispatcher = IoDispatcher()
        val workerDispatcher = WorkerDispatcher()

        val database: SQLiteDatabase by lazy<SQLiteDatabase> {
            AppSQLiteHelper(applicationContext).writableDatabase
        }

        return@init listOf(
            ioDispatcher,
            workerDispatcher,
            SQLiteUserHobbiesRepository(),
            SQLiteHobbiesRepository(database, ioDispatcher, workerDispatcher)
        )
    }
}