package com.yelysei.hobbyharbor.app.model.userhobbies

import com.yelysei.hobbyharbor.app.model.hobbies.Hobby

class SQLiteUserHobbiesRepository() : UserHobbiesRepository {
    override var userHobbies: MutableList<UserHobby> = mutableListOf()
    override fun addUserHobby(userHobby: UserHobby) {
        userHobbies.add(userHobby)
        notifyChanges()
    }

    override fun addUserHobbyExperience(uhId: Long, action: Action) {
        val userHobby: UserHobby = getUserHobbyById(uhId)
        userHobby.progress.history.add(action)
    }

    private val listeners = mutableSetOf<UserHobbiesListener>()

    override fun getUserHobbyById(id: Long): UserHobby {
        return userHobbies.first { it.id == id }
    }

    override fun addListener(listener: UserHobbiesListener) {
        listeners += listener
        listener(userHobbies)
    }

    override fun removeListener(listener: UserHobbiesListener) {
        listeners -= listener
    }

    fun notifyChanges() {
        listeners.forEach { it(userHobbies) }
    }
    init {
        userHobbies.add(
            UserHobby(
                1,
                Hobby(1,"guitar", "music", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 30, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                2,
                Hobby(1,"piano", "music", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 30, 2)
            )
        )
        userHobbies.add(
            UserHobby(
                3,
                Hobby(1,"serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                4,
                Hobby(1, "serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                5,
                Hobby(1,"serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
        userHobbies.add(
            UserHobby(
                6,
                Hobby(1, "serials", "tv", "cheap", "whatever", "whatever"),
                Progress(mutableListOf(), 129, 20)
            )
        )
    }
}
