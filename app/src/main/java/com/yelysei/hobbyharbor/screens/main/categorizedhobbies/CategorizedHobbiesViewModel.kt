package com.yelysei.hobbyharbor.screens.main.categorizedhobbies

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import kotlinx.coroutines.launch

class CategorizedHobbiesViewModel(
    private val categoryName: String,
    private val hobbiesRepository: HobbiesRepository,
    private val userHobbiesRepository: UserHobbiesRepository
) : ViewModel() {

    val _categorizedHobbies = MutableLiveResult<List<Hobby>>(PendingResult())
    val categorizedHobbies: LiveResult<List<Hobby>> = _categorizedHobbies


    init {
        load()
    }

    fun addUserHobby(hobby: Hobby, goal: Int) {

        viewModelScope.launch {
            userHobbiesRepository.addUserHobby(hobby, goal)
        }
    }

    private fun load() {
        viewModelScope.launch {
            _categorizedHobbies.value = PendingResult()
            val hobbiesList = hobbiesRepository.getAvailableHobbiesForCategory(categoryName)
            _categorizedHobbies.value = SuccessResult(hobbiesList)
        }
    }

    fun tryAgain() {
        load()
    }

}