package com.yelysei.hobbyhub.ui.screens.main.userhobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyhub.model.results.LiveResult
import com.yelysei.hobbyhub.model.results.MutableLiveResult
import com.yelysei.hobbyhub.model.results.PendingResult
import com.yelysei.hobbyhub.model.results.SuccessResult
import com.yelysei.hobbyhub.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyhub.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyhub.utils.SharedStorage
import kotlinx.coroutines.launch

class UserHobbiesViewModel(
    private val userHobbiesRepository: UserHobbiesRepository,
    private val sharedStorage: SharedStorage
) : ViewModel() {


    private val _userHobbies = MutableLiveResult<List<UserHobby>>(PendingResult())
    val userHobbies: LiveResult<List<UserHobby>> = _userHobbies


    fun load() {
        viewModelScope.launch {
            userHobbiesRepository.getUserHobbies().collect {
                _userHobbies.value = SuccessResult(it)
            }
        }
    }

    fun removeUserHobbies(userHobbies: List<UserHobby>) {
        viewModelScope.launch {
            userHobbiesRepository.deleteUserHobbies(userHobbies)
        }
        userHobbies.forEach {
            sharedStorage.removeNotificationByHobbyName(it.hobby.hobbyName)
        }
    }
}