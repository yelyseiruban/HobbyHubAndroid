package com.yelysei.hobbyharbor.screens.main.hobbydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import kotlinx.coroutines.launch

class UserHobbyDetailsViewModel(
    uhId: Int,
    private val userHobbiesRepository: UserHobbiesRepository,
) : ViewModel() {

    private val _userHobby = MutableLiveResult<UserHobby>(PendingResult())
    val userHobby: LiveResult<UserHobby> = _userHobby

    init {
        viewModelScope.launch {
            userHobbiesRepository.getUserHobbyById(uhId).collect {
                _userHobby.value = SuccessResult(it)
            }
        }
    }
}