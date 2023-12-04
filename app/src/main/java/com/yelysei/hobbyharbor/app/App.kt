package com.yelysei.hobbyharbor.app

import android.app.Application
import com.yelysei.foundation.BaseApplication
import com.yelysei.hobbyharbor.app.model.coroutines.IoDispatcher
import com.yelysei.hobbyharbor.app.model.coroutines.WorkerDispatcher
import com.yelysei.hobbyharbor.app.model.hobbies.ExternalHobbiesRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.InMemoryUserHobbiesRepository
import kotlinx.coroutines.Dispatchers

class App : Application(), BaseApplication{

    override val singletonScopeDependencies: List<Any> = listOf(
        InMemoryUserHobbiesRepository(),
        ExternalHobbiesRepository(IoDispatcher(Dispatchers.IO), WorkerDispatcher(Dispatchers.Default))
    )
}