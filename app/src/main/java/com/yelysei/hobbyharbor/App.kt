package com.yelysei.hobbyharbor

import android.app.Application
import android.content.Context
import com.yelysei.hobbyharbor.screens.main.BaseActivity
import java.util.Locale

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPreferences = this.getSharedPreferences("language", Context.MODE_PRIVATE)
        val language = sharedPreferences.getString("language", "en")
        BaseActivity.dLocale = language?.let { Locale(it) } ?: Locale("en")
    }
}