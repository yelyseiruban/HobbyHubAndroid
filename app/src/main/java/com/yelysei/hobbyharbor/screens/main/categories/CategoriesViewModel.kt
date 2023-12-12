package com.yelysei.hobbyharbor.screens.main.categories

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.hobbies.HobbiesRepository
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import kotlinx.coroutines.launch

class CategoriesViewModel(
    private val hobbiesRepository: HobbiesRepository
) : ViewModel() {
    val _categories = MutableLiveResult<List<String>>(PendingResult())
    val categories: LiveResult<List<String>> = _categories

    init {
        load()
        Log.d("debug CategoriesViewModel", categories.value.toString())
    }

    fun tryAgain() {
        load()
    }

    fun load() {
        _categories.value = PendingResult()
        viewModelScope.launch {
            hobbiesRepository.getCurrentCategories().collect {
                _categories.value = SuccessResult(it)
            }
        }
    }
}