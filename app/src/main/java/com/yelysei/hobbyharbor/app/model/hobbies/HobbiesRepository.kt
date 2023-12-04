package com.yelysei.hobbyharbor.app.model.hobbies

import com.yelysei.foundation.model.Repository

typealias HobbiesListener = (hobbies: List<Hobby>) -> Unit
typealias CategoriesListener = (categories: List<String>) -> Unit
interface HobbiesRepository : Repository {
    suspend fun getAvailableHobbies(): List<Hobby>

    suspend fun getAvailableCategories(): List<String>

    suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby>

    suspend fun addHobby(hobby: Hobby)

    fun addHobbiesListener(listener: HobbiesListener)

    fun removeHobbiesListener(listener: HobbiesListener)

    fun addCategoryListener(listener: CategoriesListener)

    fun removeCategoryListener(listener: CategoriesListener)
}