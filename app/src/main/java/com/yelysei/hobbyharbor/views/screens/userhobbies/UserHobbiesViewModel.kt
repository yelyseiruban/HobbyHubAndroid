package com.yelysei.hobbyharbor.views.screens.userhobbies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yelysei.hobbyharbor.model.UserHobby
import com.yelysei.hobbyharbor.navigator.Navigator
import com.yelysei.hobbyharbor.views.screens.base.BaseViewModel
import com.yelysei.hobbyharbor.views.screens.hobbydetails.UserHobbyDetailsFragment
import com.yelysei.hobbyharbor.service.UserHobbiesListener
import com.yelysei.hobbyharbor.service.UserHobbiesService

class UserHobbiesViewModel(
    private val navigator: Navigator,
    screen: UserHobbiesFragment.Screen,
    private val userHobbiesService: UserHobbiesService
) : BaseViewModel(){

    private val _userHobbies = MutableLiveData<List<UserHobby>>()
    val userHobbies: LiveData<List<UserHobby>> = _userHobbies

    private val listener: UserHobbiesListener = {
        _userHobbies.value = it
    }

    init {
        loadUserHobbies()
    }

    override fun onCleared() {
        super.onCleared()
        userHobbiesService.removeListener(listener)
    }

    fun loadUserHobbies() {
        userHobbiesService.addListener(listener)
    }

    fun onUserHobbyPressed(uhId: Long) {
        navigator.launch(UserHobbyDetailsFragment.Screen(uhId))
    }

}