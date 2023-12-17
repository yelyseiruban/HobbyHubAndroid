package com.yelysei.hobbyharbor.screens.main.hobbydetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.UserHobbyIsNotLoadedException
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.results.takeSuccess
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class UserHobbyDetailsViewModel(
    uhId: Int,
    private val userHobbiesRepository: UserHobbiesRepository,
) : ViewModel() {

    private val _userHobby = MutableLiveResult<UserHobby>(PendingResult())
    val userHobby: LiveResult<UserHobby> = _userHobby

    private val _actions = MutableLiveResult<List<Action>>(PendingResult())
    val actions: LiveResult<List<Action>> = _actions

    init {
        viewModelScope.launch {
            userHobbiesRepository.getUserHobbyById(uhId)
                .map { userHobby ->
                    val progressId = userHobby.progress.id
                    Pair(userHobby, progressId)
                }
                .flatMapConcat { (userHobby, progressId) ->
                    // Combine with the second flow
                    userHobbiesRepository.getActionsByProgressId(progressId)
                        .map { actions ->
                            Pair(userHobby, actions)
                        }
                }
                .collect { (userHobby, actions) ->
                    Log.d("user hobby details view model", "User Hobby: $userHobby, Actions: $actions")
                    userHobby.progress.actions = actions
                    _userHobby.value = SuccessResult(userHobby)
                    _actions.value = SuccessResult(actions)
                }
        }

    }

    fun userExperienceInteraction(interaction: UserExperienceInteraction, from: Long, till: Long, id: Int? = null) {
        val userHobby = _userHobby.value.takeSuccess() ?: throw UserHobbyIsNotLoadedException()
        val progressId = userHobby.progress.id

        val action = Action (
            id = id ?: 0,
            startTime = from,
            endTime = till
        )
        viewModelScope.launch {
            when(interaction) {
                UserExperienceInteraction.ADD -> {
                    userHobbiesRepository.addUserHobbyExperience(progressId, action)
                }
                UserExperienceInteraction.UPDATE -> {
                    userHobbiesRepository.updateUserHobbyExperience(progressId, action)
                }
            }
        }

    }

    fun addUserExperience(from: Long, till: Long) {
        userExperienceInteraction(UserExperienceInteraction.ADD, from, till)
    }

    fun editUserExperience(from: Long, till: Long, actionId: Int) {
        userExperienceInteraction(UserExperienceInteraction.UPDATE, from, till, actionId)
    }
}

enum class UserExperienceInteraction {
    UPDATE, ADD
}