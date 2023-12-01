package com.yelysei.hobbyharbor.model.hobbies

import com.yelysei.hobbyharbor.model.Repository

typealias UserHobbiesListener = (userHobbies: List<UserHobby>) -> Unit

interface UserHobbiesRepository : Repository {
    var userHobbies: MutableList<UserHobby>

    fun getUserHobbyById(id: Long): UserHobby

    fun addUserHobby(userHobby: UserHobby)

    fun addUserHobbyExperience(uhId: Long, action: Action)

    fun addListener(listener: UserHobbiesListener)

    fun removeListener(listener: UserHobbiesListener)

}