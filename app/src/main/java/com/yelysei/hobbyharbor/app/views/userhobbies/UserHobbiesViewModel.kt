package com.yelysei.hobbyharbor.app.views.userhobbies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobby
import com.yelysei.foundation.navigator.Navigator
import com.yelysei.foundation.views.BaseViewModel
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobbiesListener
import com.yelysei.hobbyharbor.app.views.hobbydetails.UserHobbyDetailsFragment

class UserHobbiesViewModel(
    private val navigator: Navigator,
    private val hobbiesRepository: UserHobbiesRepository
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
        hobbiesRepository.removeListener(listener)
    }

    private fun loadUserHobbies() {
        hobbiesRepository.addListener(listener)
    }

    fun onUserHobbyPressed(uhId: Long) {
        navigator.launch(UserHobbyDetailsFragment.Screen(uhId))
    }

}