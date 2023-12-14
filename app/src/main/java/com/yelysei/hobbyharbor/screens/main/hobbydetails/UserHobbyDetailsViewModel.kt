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
import com.yelysei.hobbyharbor.screens.dialogs.Date
import com.yelysei.hobbyharbor.screens.main.LiveResult
import com.yelysei.hobbyharbor.screens.main.MutableLiveResult
import com.yelysei.hobbyharbor.utils.getTimeInMillis
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
                    _userHobby.value = SuccessResult(userHobby)
                    _actions.value = SuccessResult(actions)
                }
        }

    }
    fun addUserExperience(from: Date, till: Date) {
        val userHobby = _userHobby.value.takeSuccess() ?: throw UserHobbyIsNotLoadedException()
        val progressId = userHobby.progress.id
        val startTime = getTimeInMillis(from.year, from.monthOfYear, from.dayOfMonth, from.hour, from.minute)
        val endTime = getTimeInMillis(till.year, till.monthOfYear, till.dayOfMonth, till.hour, till.minute)

        val action = Action (
            id = 0,
            startTime = startTime,
            endTime = endTime
        )
        viewModelScope.launch {
            userHobbiesRepository.addUserHobbyExperience(progressId, action)
        }
    }
}