package com.yelysei.hobbyharbor.app

import android.app.Application
import com.yelysei.foundation.BaseApplication
import com.yelysei.foundation.model.Repository
import com.yelysei.hobbyharbor.app.model.hobbies.ExternalRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.InMemoryUserHobbiesRepository

class App : Application(), BaseApplication{
    override val repositories: List<Repository> = listOf(
        InMemoryUserHobbiesRepository(),
        ExternalRepository()
    )
}