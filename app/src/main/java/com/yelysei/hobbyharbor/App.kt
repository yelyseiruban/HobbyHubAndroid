package com.yelysei.hobbyharbor

import android.app.Application
import com.yelysei.hobbyharbor.service.UserHobbiesService

class App : Application() {
    val userHobbiesService = UserHobbiesService()
}