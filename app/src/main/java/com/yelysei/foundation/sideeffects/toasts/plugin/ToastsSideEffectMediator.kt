package com.yelysei.foundation.sideeffects.toasts.plugin

import android.content.Context
import android.widget.Toast
import com.yelysei.foundation.model.tasks.dispatchers.MainThreadDispatcher
import com.yelysei.foundation.sideeffects.SideEffectMediator

/**
 * Android implementation of [Toasts]. Displaying simple toast message and getting string from resources.
 */
class ToastsSideEffectMediator(
    private val appContext: Context
) : SideEffectMediator<Nothing>(), Toasts {

    private val dispatcher = MainThreadDispatcher()

    override fun toast(message: String) {
        dispatcher.dispatch {
            Toast.makeText(appContext, message, Toast.LENGTH_SHORT).show()
        }
    }

}