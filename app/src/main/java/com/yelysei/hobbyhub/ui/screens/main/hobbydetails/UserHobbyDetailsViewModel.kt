package com.yelysei.hobbyhub.ui.screens.main.hobbydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyhub.model.UserHobbyIsNotLoadedException
import com.yelysei.hobbyhub.model.results.LiveResult
import com.yelysei.hobbyhub.model.results.MutableLiveResult
import com.yelysei.hobbyhub.model.results.PendingResult
import com.yelysei.hobbyhub.model.results.SuccessResult
import com.yelysei.hobbyhub.model.results.takeSuccess
import com.yelysei.hobbyhub.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyhub.model.userhobbies.entities.Experience
import com.yelysei.hobbyhub.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyhub.model.userhobbies.room.entities.ProgressDbEntity
import com.yelysei.hobbyhub.utils.NotificationState
import com.yelysei.hobbyhub.utils.SharedStorage
import kotlinx.coroutines.launch


class UserHobbyDetailsViewModel(
    uhId: Int,
    private val userHobbiesRepository: UserHobbiesRepository,
    private val sharedStorage: SharedStorage
) : ViewModel() {

    private val _userHobby = MutableLiveResult<UserHobby>(PendingResult())
    val userHobby: LiveResult<UserHobby> = _userHobby

    init {
        viewModelScope.launch {
            userHobbiesRepository.getUserHobbyById(uhId).collect {
                _userHobby.value = SuccessResult(it)
            }
        }
    }

    private fun userExperienceInteraction(
        interaction: UserExperienceInteraction,
        from: Long,
        till: Long,
        id: Int? = null
    ) {
        val userHobby = _userHobby.value.takeSuccess() ?: throw UserHobbyIsNotLoadedException()
        val progressId = userHobby.progress.id

        val experience = Experience(
            id = id ?: 0,
            startTime = from,
            endTime = till
        )
        viewModelScope.launch {
            when (interaction) {
                UserExperienceInteraction.ADD -> {
                    userHobbiesRepository.addUserHobbyExperience(progressId, experience)
                }

                UserExperienceInteraction.UPDATE -> {
                    userHobbiesRepository.updateUserHobbyExperience(progressId, experience)
                }
            }
        }

    }

    fun addUserExperience(from: Long, till: Long) {
        userExperienceInteraction(UserExperienceInteraction.ADD, from, till)
    }

    fun editUserExperience(from: Long, till: Long, experienceId: Int) {
        userExperienceInteraction(UserExperienceInteraction.UPDATE, from, till, experienceId)
    }

    fun updateProgress(goal: Int) {
        val userHobby = _userHobby.value.takeSuccess() ?: throw UserHobbyIsNotLoadedException()
        val progressId = userHobby.progress.id

        val progressDbEntity = ProgressDbEntity(
            id = progressId,
            goal = goal
        )
        viewModelScope.launch {
            userHobbiesRepository.updateProgress(progressDbEntity)
        }
    }

    fun deActiveNotification() {
        val hobbyName = _userHobby.value.takeSuccess()?.hobby?.hobbyName
            ?: throw java.lang.IllegalStateException("User hobby is not loaded")
        sharedStorage.removeNotificationByHobbyName(hobbyName)
    }

    fun activeNotification(internalTimeInMilliseconds: Long) {
        val hobbyName = _userHobby.value.takeSuccess()?.hobby?.hobbyName
            ?: throw java.lang.IllegalStateException("User hobby is not loaded")
        val hobbyId = _userHobby.value.takeSuccess()!!.hobby.id!!

        val updatedNotificationState = NotificationState(
            hobbyName, "channelId $hobbyName", hobbyId, internalTimeInMilliseconds, true
        )
        sharedStorage.changeOrAddNotificationStateObject(updatedNotificationState)
    }

    fun getNotificationId(): Int {
        return _userHobby.value.takeSuccess()?.id ?: throw IllegalStateException("User hobby is not loaded")
    }

    fun previousInternalTimeInMilliseconds(): Long? {
        val hobbyName = _userHobby.value.takeSuccess()!!.hobby.hobbyName
        return sharedStorage.getInternalTimeInMillisecondsByHobbyName(hobbyName)
    }
}

enum class UserExperienceInteraction {
    UPDATE, ADD
}
