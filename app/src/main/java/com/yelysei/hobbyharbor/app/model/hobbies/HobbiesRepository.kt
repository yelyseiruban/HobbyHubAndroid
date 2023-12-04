package com.yelysei.hobbyharbor.app.model.hobbies

import com.yelysei.foundation.model.Repository
import com.yelysei.foundation.model.tasks.Task

typealias HobbiesListener = (hobbies: List<Hobby>) -> Unit
typealias CategoriesListener = (categories: List<String>) -> Unit
interface HobbiesRepository : Repository {
    fun getAvailableHobbies(): Task<List<Hobby>>

    fun getAvailableCategories(): Task<List<String>>

    fun getAvailableHobbiesForCategory(categoryName: String): Task<List<Hobby>>

    fun addHobby(hobby: Hobby): Task<Unit>

    fun addHobbiesListener(listener: HobbiesListener)

    fun removeHobbiesListener(listener: HobbiesListener)

    fun addCategoryListener(listener: CategoriesListener)

    fun removeCategoryListener(listener: CategoriesListener)
}