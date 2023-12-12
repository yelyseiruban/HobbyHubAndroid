package com.yelysei.hobbyharbor.screens.main.hobbydetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours

class UserHobbyDetailsViewModel(
    private val userHobbiesRepository: UserHobbiesRepository,
) : ViewModel() {

    private val _userHobby =  MutableLiveData<UserHobby>()
    val userHobby: LiveData<UserHobby> = _userHobby

    fun load(uhId: Int) {
//        viewModelScope.launch {
//            _userHobby.value = userHobbiesRepository.getUserHobbyById(uhId)
//        }
    }

    fun getUserHobbyName() = userHobby.value?.hobby?.hobbyName
    fun getTotalValue() = userHobby.value?.getProgressInHours()
    fun getGoalValue() = userHobby.value?.progress?.goal
}