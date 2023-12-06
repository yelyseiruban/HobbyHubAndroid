package com.yelysei.hobbyharbor.app.views.categories

import com.yelysei.foundation.model.PendingResult
import com.yelysei.foundation.sideeffects.toasts.plugin.Toasts
import com.yelysei.foundation.views.BaseViewModel
import com.yelysei.foundation.views.LiveResult
import com.yelysei.foundation.views.MutableLiveResult
import com.yelysei.hobbyharbor.app.model.hobbies.HobbiesRepository

class CategoriesViewModel(
    private val toasts: Toasts,
    private val hobbiesRepository: HobbiesRepository
) : BaseViewModel() {

    private val _categories = MutableLiveResult<List<String>>(PendingResult())
    val categories: LiveResult<List<String>> = _categories

    init {
//        viewModelScope.launch {
//            hobbiesRepository.listenCurrentHobbies().collect() {
//                _categories.postValue(SuccessResult(hobbiesRepository.getAvailableCategories()))
//            }
//        }
        load()
    }

    fun onCategoryClick(categoryName: String) {
        toasts.toast("$categoryName was clicked")
    }

    fun tryAgain() {
        load()
    }

    private fun load() = into(_categories) {
        _categories.value = PendingResult()
        hobbiesRepository.getAvailableCategories()
    }

}