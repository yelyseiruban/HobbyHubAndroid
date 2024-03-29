package com.yelysei.hobbyhub

import android.content.Context
import com.yelysei.hobbyhub.utils.SharedStorage

object SharedPreferences{

    private lateinit var applicationContext: Context

    val sharedStorage: SharedStorage by lazy {
        SharedStorage(applicationContext.getSharedPreferences(SharedStorage.STORE_FILE_NAME, Context.MODE_PRIVATE))
    }

    fun init (context: Context) {
        applicationContext = context
    }
}