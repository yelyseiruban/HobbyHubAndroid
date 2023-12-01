package com.yelysei.hobbyharbor.views.userhobbies

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.yelysei.hobbyharbor.model.hobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.UserHobbiesListener
import com.yelysei.hobbyharbor.model.hobbies.UserHobby
import com.yelysei.hobbyharbor.views.Navigator
import com.yelysei.hobbyharbor.views.base.BaseViewModel
import com.yelysei.hobbyharbor.views.hobbydetails.UserHobbyDetailsFragment

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