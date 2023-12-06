package com.yelysei.hobbyharbor

import com.yelysei.foundation.SingletonScopeDependencies
import com.yelysei.hobbyharbor.app.model.coroutines.IoDispatcher
import com.yelysei.hobbyharbor.app.model.coroutines.WorkerDispatcher
import com.yelysei.hobbyharbor.app.model.hobbies.ExternalHobbiesRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.InMemoryUserHobbiesRepository

object Initializer {

    fun initDependencies() = SingletonScopeDependencies.init { applicationContext ->

        val ioDispatcher = IoDispatcher()
        val workerDispatcher = WorkerDispatcher()

        return@init listOf(
            ioDispatcher,
            workerDispatcher,
            InMemoryUserHobbiesRepository(),
            ExternalHobbiesRepository(ioDispatcher, workerDispatcher)
        )
    }
}