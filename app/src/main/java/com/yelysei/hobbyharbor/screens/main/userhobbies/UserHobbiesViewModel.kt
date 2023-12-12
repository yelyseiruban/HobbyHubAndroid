package com.yelysei.hobbyharbor.screens.main.userhobbies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.NoUserHobbiesFoundException
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
            try {
                userHobbiesRepository.getUserHobbies().collect {
                    _userHobbies.value = SuccessResult(it)
                }
            } catch (e: NoUserHobbiesFoundException) {
                Log.d("Exception", "No User Hobbies Found Exception")
                _userHobbies.value = SuccessResult(listOf())
            }
        }
    }
}