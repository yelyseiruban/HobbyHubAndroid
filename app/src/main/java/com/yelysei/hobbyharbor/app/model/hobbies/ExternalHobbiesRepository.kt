package com.yelysei.hobbyharbor.app.model.hobbies

import com.yelysei.hobbyharbor.app.model.coroutines.IoDispatcher
import com.yelysei.hobbyharbor.app.model.coroutines.WorkerDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

class ExternalHobbiesRepository(
    private val ioDispatcher: IoDispatcher,
    private val workerDispatcher: WorkerDispatcher
) : HobbiesRepository {
    private val hobbiesListeners = mutableSetOf<HobbiesListener>()
    private val categoriesListeners = mutableSetOf<CategoriesListener>()

    init {
//        categories = getAvailableCategories()
    }

    override suspend fun getAvailableHobbies(): List<Hobby> = withContext(ioDispatcher.value) {
        delay(1000)
        return@withContext AVAILABLE_HOBBIES
    }

    override suspend fun getAvailableCategories(): List<String> = withContext(workerDispatcher.value){
        delay(1000)
        val categories = mutableSetOf<String>()
        AVAILABLE_HOBBIES.forEach { hobby: Hobby ->  categories.add(hobby.categoryName) }
        return@withContext categories.toList()
    }

    override suspend fun getAvailableHobbiesForCategory(categoryName: String): List<Hobby> = withContext(workerDispatcher.value) {
        delay(1000)
        return@withContext AVAILABLE_HOBBIES.filter { hobby: Hobby -> hobby.categoryName == categoryName }
    }

    override suspend fun addHobby(hobby: Hobby) = withContext(ioDispatcher.value) {
        delay(1000)
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
//        categoriesListeners.forEach { it(getAvailableCategories()) }
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