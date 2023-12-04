package com.yelysei.hobbyharbor.app

import android.app.Application
import com.yelysei.foundation.BaseApplication
import com.yelysei.foundation.model.tasks.ThreadUtils
import com.yelysei.foundation.model.tasks.dispatchers.MainThreadDispatcher
import com.yelysei.foundation.model.tasks.factories.ExecutorServiceTasksFactory
import com.yelysei.hobbyharbor.app.model.hobbies.ExternalHobbiesRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.InMemoryUserHobbiesRepository
import java.util.concurrent.Executors

class App : Application(), BaseApplication{

//    private val tasksFactory = ThreadTaskFactory()
    private val tasksFactory = ExecutorServiceTasksFactory(Executors.newCachedThreadPool())

    private val threadUtils = ThreadUtils.Default()
    private val dispatcher = MainThreadDispatcher()

    override val singletonScopeDependencies: List<Any> = listOf(
        tasksFactory,
        dispatcher,
        InMemoryUserHobbiesRepository(),
        ExternalHobbiesRepository(tasksFactory, threadUtils)
    )
}