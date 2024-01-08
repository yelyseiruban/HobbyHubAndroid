package com.yelysei.hobbyharbor.ui.screens.main.experiencedetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yelysei.hobbyharbor.model.results.LiveResult
import com.yelysei.hobbyharbor.model.results.MutableLiveResult
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.userhobbies.UserHobbiesRepository
import com.yelysei.hobbyharbor.model.userhobbies.entities.Experience
import com.yelysei.hobbyharbor.model.userhobbies.entities.ImageReference
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

class ExperienceDetailsViewModel(
    private val experienceId: Int,
    private val userHobbiesRepository: UserHobbiesRepository
) : ViewModel() {

    private val _experiencePin = MutableLiveResult<ExperiencePin>(PendingResult())
    val experiencePin: LiveResult<ExperiencePin> = _experiencePin

    private val _isEditState = MutableLiveData(false)
    val isEditState: LiveData<Boolean> = _isEditState

    init {
        viewModelScope.launch {
            combine(
                userHobbiesRepository.getUserExperienceById(experienceId),
                userHobbiesRepository.getUriReferencesByExperienceId(experienceId)
            ) { experience, imageReferences ->
                ExperiencePin(experience, imageReferences)
            }.collect { combinedExperiencePin ->
                _experiencePin.value = SuccessResult(combinedExperiencePin)
            }
        }
    }

    fun changeEditState() {
        _isEditState.value = _isEditState.value != true
    }

    fun savePin(noteInputText: String?, selectedImageUris: List<String>?) {
        viewModelScope.launch {
            if (noteInputText != null) {
                userHobbiesRepository.updateNoteTextByExperienceId(noteInputText, experienceId)
            }
            if (selectedImageUris != null) {
                userHobbiesRepository.insertUriReferences(selectedImageUris, experienceId)
            }
        }
    }

    fun removeImageUris(imageReferences: List<ImageReference>) {
        viewModelScope.launch {
            userHobbiesRepository.deleteImageReferences(imageReferences)
        }
    }

}

data class ExperiencePin(
    val experience: Experience,
    val imageReferences: List<ImageReference>
)