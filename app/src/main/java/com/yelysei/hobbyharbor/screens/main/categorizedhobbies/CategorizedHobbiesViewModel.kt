package com.yelysei.hobbyharbor.screens.main.categorizedhobbies

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.HobbyAlreadyExistsException
import com.yelysei.hobbyharbor.model.UserHobbyAlreadyAddedException
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import com.yelysei.hobbyharbor.screens.uiactions.UiActions
import com.yelysei.hobbyharbor.utils.CustomTypeface
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CategorizedHobbiesViewModel(
    private val hobbiesRepository: HobbiesRepository,
    private val userHobbiesRepository: UserHobbiesRepository,
    private val uiActions: UiActions
) : ViewModel() {

    private val _categorizedHobbies = MutableLiveResult<List<Hobby>>(PendingResult())
    val categorizedHobbies: LiveResult<List<Hobby>> = _categorizedHobbies

    private val _searchedHobbies = MutableLiveResult<List<Hobby>>(SuccessResult(emptyList()))
    val searchedHobbies: LiveResult<List<Hobby>> = _searchedHobbies

    private val _categories = MutableLiveResult<List<String>>(PendingResult())
    val categories: LiveResult<List<String>> = _categories


    init {
        load()
    }

    fun searchHobbies(hobbyNameSearchInput: String) {
        if (hobbyNameSearchInput != "") {
            Log.d("viewModel", hobbyNameSearchInput)
            viewModelScope.launch {
                _searchedHobbies.value = PendingResult()
                val hobbiesList = hobbiesRepository.getHobbiesByHobbyName(hobbyNameSearchInput)

                Log.d("viewModel", hobbiesList.toString())
                _searchedHobbies.value = SuccessResult(hobbiesList)
            }
        }
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
            combine(
                hobbiesRepository.getCurrentHobbies(),
                hobbiesRepository.getCurrentCategories()
            ) { categorizedHobbies, categories ->
                _categorizedHobbies.value = SuccessResult(categorizedHobbies)
                val capitalizedCategories = categories.map {
                    CustomTypeface.capitalizeEachWord(it).toString()
                }
                _categories.value = SuccessResult(capitalizedCategories)
            }.collect()
        }
    }

    fun tryAgain() {
        load()
    }

    fun addCustomHobby(hobby: Hobby, goal: Int) {
        viewModelScope.launch {
            try {
                val id = hobbiesRepository.addCustomHobby(hobby)
                hobby.id = id
                addUserHobby(hobby, goal)
            } catch (e: HobbyAlreadyExistsException) {
                uiActions.toast("The hobby you are trying to add already exists")
            }
        }
    }

}