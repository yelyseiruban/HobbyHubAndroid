package com.yelysei.hobbyharbor.app.views.hobbydetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.yelysei.hobbyharbor.R
import com.yelysei.foundation.model.UserHobbyNotFoundException
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobby
import com.yelysei.foundation.navigator.Navigator
import com.yelysei.foundation.views.BaseViewModel
import com.yelysei.foundation.uiactions.UiActions

class UserHobbyDetailsViewModel(
    screen: UserHobbyDetailsFragment.Screen,
    private val navigator: Navigator,
    private val uiActions: UiActions,
    hobbiesRepository: UserHobbiesRepository,
    savedStateHandle: SavedStateHandle
) : BaseViewModel(){

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
        uiActions.toast(R.string.goal_edit)
    }

    fun onAddExperience() {
        uiActions.toast(R.string.add_experience)
    }

    fun onEditAction() {
        uiActions.toast(R.string.edit_action)
    }

    fun onBackPressed() {
        navigator.goBack()
    }

    fun onNotificationPressed() {
        uiActions.toast(R.string.notification_button_pressed)
    }
}