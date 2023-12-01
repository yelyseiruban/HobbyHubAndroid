package com.yelysei.hobbyharbor.service

import com.yelysei.hobbyharbor.exceptions.UserHobbyNotFoundException
import com.yelysei.hobbyharbor.model.Hobby
import com.yelysei.hobbyharbor.model.Progress
import com.yelysei.hobbyharbor.model.UserHobby

typealias UserHobbiesListener = (userHobbies: List<UserHobby>) -> Unit

class UserHobbiesService {
    private var userHobbies = mutableListOf<UserHobby>()
    private val listeners = mutableSetOf<UserHobbiesListener>()
    init {
        userHobbies.add(
            UserHobby(
                1,
                Hobby("guitar", "music", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 30, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                2,
                Hobby("piano", "music", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 30, 2)
            )
        )
        userHobbies.add(
            UserHobby(
                3,
                Hobby("serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                4,
                Hobby("serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                5,
                Hobby("serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                6,
                Hobby("serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
    }

    fun getUserHobbies(): List<UserHobby> {
        return userHobbies;
    }

    fun getById(id: Long): UserHobby {
        return userHobbies.firstOrNull { it.id == id } ?: throw UserHobbyNotFoundException()
    }

    fun deleteUserHobby(userHobby: UserHobby) {
        val indexToDelete = userHobbies.indexOfFirst { it.id == userHobby.id };
        if (indexToDelete != -1) {
            userHobbies.removeAt(indexToDelete);
            notifyChanges()
        }
    }

    fun addListener(listener: UserHobbiesListener) {
        listeners.add(listener)
        listener.invoke(userHobbies)
    }

    fun removeListener(listener: UserHobbiesListener) {
        listeners.remove(listener);
    }

    private fun notifyChanges() {
        listeners.forEach{ it.invoke(userHobbies) }
    }
}