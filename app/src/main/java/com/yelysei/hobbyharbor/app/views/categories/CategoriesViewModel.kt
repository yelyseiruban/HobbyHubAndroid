package com.yelysei.hobbyharbor.app.views.categories

import com.yelysei.foundation.model.PendingResult
import com.yelysei.foundation.model.SuccessResult
import com.yelysei.foundation.model.tasks.dispatchers.Dispatcher
import com.yelysei.foundation.sideeffects.navigator.Navigator
import com.yelysei.foundation.sideeffects.resources.Resources
import com.yelysei.foundation.sideeffects.toasts.plugin.Toasts
import com.yelysei.foundation.views.BaseViewModel
import com.yelysei.foundation.views.LiveResult
import com.yelysei.foundation.views.MutableLiveResult
import com.yelysei.hobbyharbor.app.model.hobbies.CategoriesListener
import com.yelysei.hobbyharbor.app.model.hobbies.HobbiesRepository

class CategoriesViewModel(
    private val navigator: Navigator,
    private val toasts: Toasts,
    private val resources: Resources,
    private val hobbiesRepository: HobbiesRepository,
    dispatcher: Dispatcher
) : BaseViewModel(dispatcher) {

    private val _categories = MutableLiveResult<List<String>>(PendingResult())
    val categories: LiveResult<List<String>> = _categories

    private val categoriesListener: CategoriesListener = {categories ->
        _categories.postValue(SuccessResult(categories))
    }

    init {
        hobbiesRepository.addCategoryListener(categoriesListener)
        load()
    }

    fun onCategoryClick(categoryName: String) {
        toasts.toast("$categoryName was clicked")
    }

    fun tryAgain() {
        load()
    }

    private fun load() {
        hobbiesRepository.getAvailableCategories().into(_categories)
    }

}