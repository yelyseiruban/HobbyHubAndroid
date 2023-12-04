package com.yelysei.hobbyharbor.app.main

import com.yelysei.foundation.model.tasks.dispatchers.Dispatcher
import com.yelysei.foundation.sideeffects.navigator.Navigator
import com.yelysei.foundation.sideeffects.toasts.plugin.Toasts
import com.yelysei.foundation.views.BaseViewModel

class MainActivityViewModel(
    private val navigator: Navigator,
    private val toasts: Toasts,
    dispatcher: Dispatcher
) : BaseViewModel(dispatcher) {

    fun onSettingsPressed() {
        toasts.toast("Settings button pressed")
    }

    fun onUserProfilePressed() {
        toasts.toast("User Profile button pressed")

    }

}