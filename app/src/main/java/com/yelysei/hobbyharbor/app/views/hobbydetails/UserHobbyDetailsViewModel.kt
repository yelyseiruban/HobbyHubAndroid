package com.yelysei.hobbyharbor.app.views.hobbydetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yelysei.foundation.model.UserHobbyNotFoundException
import com.yelysei.foundation.model.tasks.dispatchers.Dispatcher
import com.yelysei.foundation.sideeffects.navigator.Navigator
import com.yelysei.foundation.sideeffects.toasts.plugin.Toasts
import com.yelysei.foundation.views.BaseViewModel
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobby

class UserHobbyDetailsViewModel(
    screen: UserHobbyDetailsFragment.Screen,
    private val navigator: Navigator,
    private val toasts: Toasts,
    dispatcher: Dispatcher,
    hobbiesRepository: UserHobbiesRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(dispatcher){

    private val _userHobbyId = savedStateHandle.getLiveData("userHobby", screen.uhId)
    private val _userHobby = MutableLiveData<UserHobby>()
    val userHobby: LiveData<UserHobby> = _userHobby

    init {
        try {
            _userHobby.value = hobbiesRepository.getUserHobbyById(_userHobbyId.value!!)
        } catch (e: UserHobbyNotFoundException) {
            e.printStackTrace()
        }
    }

    fun onGoalEdit() {
        toasts.toast("Goal Edit Pressed")
    }

    fun onAddExperience() {
        toasts.toast("Add Experience Pressed")
    }

    fun onEditAction() {
        toasts.toast("Edit Action Pressed")
    }

    override fun onBackPressed(): Boolean {
        navigator.goBack()
        return false
    }

    fun onNotificationPressed() {
        toasts.toast("Notification button pressed")
    }
}