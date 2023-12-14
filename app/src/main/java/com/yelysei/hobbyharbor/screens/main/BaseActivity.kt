package com.yelysei.hobbyharbor.screens.main

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import java.util.Locale

open class BaseActivity : AppCompatActivity() {

    companion object {
        var dLocale: Locale? = null
    }

    init {
        updateConfig(this as ContextThemeWrapper)
    }

    fun updateConfig(wrapper: ContextThemeWrapper) {
        if(dLocale == Locale("") ) // Do nothing if dLocale is null
            return

        Locale.setDefault(dLocale ?: return)
        val configuration = Configuration()
        configuration.setLocale(dLocale)
        wrapper.applyOverrideConfiguration(configuration)
    }
}