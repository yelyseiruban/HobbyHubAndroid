package com.yelysei.hobbyharbor.views.screens.hobbydetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.exceptions.UserHobbyNotFoundException
import com.yelysei.hobbyharbor.model.UserHobby
import com.yelysei.hobbyharbor.navigator.Navigator
import com.yelysei.hobbyharbor.views.screens.base.BaseViewModel
import com.yelysei.hobbyharbor.service.UserHobbiesService

class UserHobbyDetailsViewModel(
    private val navigator: Navigator,
    screen: UserHobbyDetailsFragment.Screen,
    private val userHobbiesService: UserHobbiesService
) : BaseViewModel(){

    private val _userHobby = MutableLiveData<UserHobby>()
    val userHobby: LiveData<UserHobby> = _userHobby

    init {
        try {
            _userHobby.value = userHobbiesService.getById(screen.uhId);
        } catch (e: UserHobbyNotFoundException) {
            e.printStackTrace()
        }
    }

    fun onGoalEdit() {
        navigator.toast(R.string.goal_edit)
    }

    fun onAddExperience() {
        navigator.toast(R.string.add_experience)
    }

    fun onEditAction() {
        navigator.toast(R.string.edit_action)
    }

    fun onBackPressed() {
        navigator.goBack()
    }
}