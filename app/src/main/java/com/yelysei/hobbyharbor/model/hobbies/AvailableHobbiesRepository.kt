package com.yelysei.hobbyharbor.model.hobbies

class AvailableHobbiesRepository : ExternalHobbiesRepository {
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