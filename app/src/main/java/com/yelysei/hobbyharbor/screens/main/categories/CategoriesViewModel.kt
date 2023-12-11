package com.yelysei.hobbyharbor.screens.main.categories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.results.LiveResult
import com.yelysei.hobbyharbor.model.results.MutableLiveResult
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val hobbiesRepository: HobbiesRepository
) : ViewModel() {
    val _categories = MutableLiveResult<List<String>>(PendingResult())
    val categories: LiveResult<List<String>> = _categories

    init {
        viewModelScope.launch {
            _categories.value = SuccessResult(hobbiesRepository.getCurrentCategories())
        }
    }
}