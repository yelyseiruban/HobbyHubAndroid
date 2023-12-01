package com.yelysei.hobbyharbor

import android.app.Application
import com.yelysei.hobbyharbor.model.hobbies.AvailableHobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.InMemoryUserHobbiesRepository

class App : Application() {
    var models = listOf(
        InMemoryUserHobbiesRepository(),
        AvailableHobbiesRepository()
    )
}