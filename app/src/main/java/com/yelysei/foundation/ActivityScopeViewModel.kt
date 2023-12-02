package com.yelysei.foundation

import androidx.lifecycle.ViewModel
import com.yelysei.foundation.navigator.IntermediateNavigator
import com.yelysei.foundation.navigator.Navigator
import com.yelysei.foundation.uiactions.UiActions

const val ARG_SCREEN = "ARG_SCREEN"
class ActivityScopeViewModel(
    val uiActions: UiActions,
    val navigator: IntermediateNavigator
) : ViewModel(), Navigator by navigator, UiActions by uiActions {

    override fun onCleared() {
        super.onCleared()
        navigator.clear()
    }
    fun onSettingsPressed() {
//        toast(R.string.settings_pressed)
    }

    fun onUserProfilePressed() {
//        toast(R.string.user_profile_pressed)
    }

}
