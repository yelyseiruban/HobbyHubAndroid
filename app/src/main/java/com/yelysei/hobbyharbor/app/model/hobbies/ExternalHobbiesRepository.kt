package com.yelysei.hobbyharbor.app.model.hobbies

import com.yelysei.foundation.model.Repository
import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobby

interface ExternalHobbiesRepository : Repository {
    var hobbies: List<Hobby>
    fun getUserHobbyById(id: Long): UserHobby

    fun getAvailableHobbies(): List<Hobby>

    fun getAvailableCategories(): List<String>

    fun getAvailableHobbiesForCategory(): List<Hobby>
}