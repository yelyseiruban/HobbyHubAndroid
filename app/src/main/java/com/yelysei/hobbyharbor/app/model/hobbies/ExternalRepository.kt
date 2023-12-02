package com.yelysei.hobbyharbor.app.model.hobbies

import com.yelysei.hobbyharbor.app.model.userhobbies.UserHobby

class ExternalRepository : ExternalHobbiesRepository {
    override var hobbies: List<Hobby>
        get() = TODO("Not yet implemented")
        set(value) {}

    override fun getUserHobbyById(id: Long): UserHobby {
        TODO("Not yet implemented")
    }

    override fun getAvailableHobbies(): List<Hobby> {
        TODO("Not yet implemented")
    }

    override fun getAvailableCategories(): List<String> {
        TODO("Not yet implemented")
    }

    override fun getAvailableHobbiesForCategory(): List<Hobby> {
        TODO("Not yet implemented")
    }


}