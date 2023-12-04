package com.yelysei.hobbyharbor.app.model.hobbies

import com.yelysei.foundation.model.tasks.Task
import com.yelysei.foundation.model.tasks.factories.TasksFactory
import com.yelysei.foundation.model.tasks.ThreadUtils

class ExternalHobbiesRepository (
    private val tasksFactory: TasksFactory,
    private val threadUtils: ThreadUtils
) : HobbiesRepository {
    private val hobbiesListeners = mutableSetOf<HobbiesListener>()
    private val categoriesListeners = mutableSetOf<CategoriesListener>()

    override fun getAvailableHobbies(): Task<List<Hobby>> = tasksFactory.async {
        threadUtils.sleep(1000)
        return@async AVAILABLE_HOBBIES
    }

    override fun getAvailableCategories(): Task<List<String>>  = tasksFactory.async {
        threadUtils.sleep(1000)
        val categories = mutableSetOf<String>()
        AVAILABLE_HOBBIES.forEach { hobby: Hobby ->  categories.add(hobby.categoryName) }
        return@async categories.toList()
    }

    override fun getAvailableHobbiesForCategory(categoryName: String): Task<List<Hobby>> = tasksFactory.async {
        threadUtils.sleep(1000)
        return@async AVAILABLE_HOBBIES.filter { hobby: Hobby -> hobby.categoryName == categoryName }
    }

    override fun addHobby(hobby: Hobby): Task<Unit> = tasksFactory.async {
        threadUtils.sleep(1000)
        AVAILABLE_HOBBIES.add(hobby)
        notifyChanges()
    }

    override fun addHobbiesListener(listener: HobbiesListener) {
        hobbiesListeners += listener
    }

    override fun removeHobbiesListener(listener: HobbiesListener) {
        hobbiesListeners -= listener
    }

    override fun addCategoryListener(listener: CategoriesListener) {
        categoriesListeners += listener
    }

    override fun removeCategoryListener(listener: CategoriesListener) {
        categoriesListeners -= listener
    }

    fun notifyChanges() {
        hobbiesListeners.forEach { it(AVAILABLE_HOBBIES) }
        categoriesListeners.forEach { it(getAvailableCategories().await()) }
    }

    companion object {
        private val AVAILABLE_HOBBIES = mutableListOf(
            (Hobby(1, "guitar", "music", "low", "anywhere", "whatever")),
            (Hobby(2, "piano", "music", "low", "anywhere", "whatever")),
            (Hobby(3, "bass", "music", "low", "anywhere", "whatever")),
            (Hobby(4, "walk", "outdoor", "low", "anywhere", "whatever")),
            (Hobby(5, "mock", "mock1", "low", "anywhere", "whatever")),
            (Hobby(6, "mock", "mock2", "low", "anywhere", "whatever")),
            (Hobby(7, "mock", "mock3", "low", "anywhere", "whatever")),
        )

    }
}