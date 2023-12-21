package com.yelysei.hobbyharbor.screens.main.userhobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import kotlinx.coroutines.launch

class UserHobbiesViewModel(
    private val userHobbiesRepository: UserHobbiesRepository
) : ViewModel(){


    private val _userHobbies = MutableLiveResult<List<UserHobby>>(PendingResult())
    val userHobbies: LiveResult<List<UserHobby>> = _userHobbies


    fun load() {
        viewModelScope.launch {
            userHobbiesRepository.getUserHobbies().collect {
                _userHobbies.value = SuccessResult(it)
            }
        }
    }

    fun removeUserHobby(userHobby: UserHobby) {
        viewModelScope.launch {
            userHobbiesRepository.deleteUserHobby(userHobby)
        }
    }
}