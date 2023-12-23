package com.yelysei.hobbyharbor.screens.main.categorizedhobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.UserHobbyAlreadyAddedException
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import com.yelysei.hobbyharbor.screens.uiactions.UiActions
import kotlinx.coroutines.launch

class CategorizedHobbiesViewModel(
    private val hobbiesRepository: HobbiesRepository,
    private val userHobbiesRepository: UserHobbiesRepository,
    private val uiActions: UiActions
) : ViewModel() {

    val _categorizedHobbies = MutableLiveResult<List<Hobby>>(PendingResult())
    val categorizedHobbies: LiveResult<List<Hobby>> = _categorizedHobbies


    init {
        load()
    }

    fun addUserHobby(hobby: Hobby, goal: Int) {
        viewModelScope.launch {
            try {
                userHobbiesRepository.addUserHobby(hobby, goal)
                uiActions.toast("New hobby has been added")
            } catch (e: UserHobbyAlreadyAddedException) {
                uiActions.toast("Hobby is already added")
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            hobbiesRepository.getCurrentHobbies().collect{
                _categorizedHobbies.value = SuccessResult(it)
            }
        }
    }

    fun tryAgain() {
        load()
    }

}