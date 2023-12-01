package com.yelysei.hobbyharbor.model.hobbies

import com.yelysei.hobbyharbor.model.Repository

interface ExternalHobbiesRepository : Repository {
    var hobbies: List<Hobby>
    fun getUserHobbyById(id: Long): UserHobby

    fun getAvailableHobbies(): List<Hobby>

    fun getAvailableCategories(): List<String>

    fun getAvailableHobbiesForCategory(): List<Hobby>
}