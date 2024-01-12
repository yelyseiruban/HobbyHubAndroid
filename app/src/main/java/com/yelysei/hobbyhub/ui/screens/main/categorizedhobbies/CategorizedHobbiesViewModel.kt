package com.yelysei.hobbyhub.ui.screens.main.categorizedhobbies

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyhub.R
import com.yelysei.hobbyhub.model.HobbyAlreadyExistsException
import com.yelysei.hobbyhub.model.UserHobbyAlreadyAddedException
import com.yelysei.hobbyhub.model.hobbies.HobbiesRepository
import com.yelysei.hobbyhub.model.hobbies.entities.Hobby
import com.yelysei.hobbyhub.model.results.LiveResult
import com.yelysei.hobbyhub.model.results.MutableLiveResult
import com.yelysei.hobbyhub.model.results.PendingResult
import com.yelysei.hobbyhub.model.results.SuccessResult
import com.yelysei.hobbyhub.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyhub.ui.screens.uiactions.UiActions
import com.yelysei.hobbyhub.utils.CustomTypeface
import com.yelysei.hobbyhub.utils.resources.StringResources
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class CategorizedHobbiesViewModel(
    private val hobbiesRepository: HobbiesRepository,
    private val userHobbiesRepository: UserHobbiesRepository,
    private val uiActions: UiActions,
    private val stringResources: StringResources
) : ViewModel() {

    private val _categorizedHobbies = MutableLiveResult<List<Hobby>>(PendingResult())
    val categorizedHobbies: LiveResult<List<Hobby>> = _categorizedHobbies

    private val _searchedHobbies = MutableLiveResult<List<Hobby>>(SuccessResult(emptyList()))
    val searchedHobbies: LiveResult<List<Hobby>> = _searchedHobbies

    private val _categories = MutableLiveResult<List<String>>(PendingResult())
    val categories: LiveResult<List<String>> = _categories

    private val _searchViewFocused = MutableLiveData(false)
    val searchViewFocused: LiveData<Boolean> = _searchViewFocused


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
                uiActions.toast(stringResources.getString(R.string.user_hobby_added_confirmation))
            } catch (e: UserHobbyAlreadyAddedException) {
                uiActions.toast(stringResources.getString(R.string.hobby_already_exists))
            }
        }
    }

    private fun load() {
        viewModelScope.launch {
            combine(
                hobbiesRepository.currentHobbiesFlow,
                hobbiesRepository.currentCategoriesFlow
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
                uiActions.toast(stringResources.getString(R.string.hobby_already_exists))
            }
        }
    }

    fun checkIfHobbyExists(hobbyName: String): Boolean {
        var hobbyExists = false
        viewModelScope.launch {
            hobbyExists = hobbiesRepository.hobbyExists(hobbyName)
        }
        return hobbyExists
    }

    fun checkIfUserHobbyExists(hobbyId: Int): LiveData<Boolean> {
        val result = MutableLiveData<Boolean>()

        viewModelScope.launch {
            result.postValue(userHobbiesRepository.userHobbyExists(hobbyId))
        }
        return result
    }

    fun setSearchViewFocused(isFocused: Boolean) {
        _searchViewFocused.value = isFocused
    }

}