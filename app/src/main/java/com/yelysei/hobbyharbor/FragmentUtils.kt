package com.yelysei.hobbyharbor

import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yelysei.hobbyharbor.navigator.Navigator
import com.yelysei.hobbyharbor.views.screens.userhobbies.UserHobbiesViewModel
import com.yelysei.hobbyharbor.views.screens.hobbydetails.UserHobbyDetailsViewModel

class ViewModelFactory (
    private val app: App
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val viewModel = when(modelClass) {
            UserHobbiesViewModel::class.java -> {
                UserHobbiesViewModel(app.userHobbiesService)
            }
            UserHobbyDetailsViewModel::class.java -> {
                UserHobbyDetailsViewModel(app.userHobbiesService)
            }
            else -> {
                throw IllegalArgumentException("Unknown view model class")
            }
        }
        return viewModel as T
    }
}

fun Fragment.factory() = ViewModelFactory(requireContext().applicationContext as App)

fun Fragment.navigator() = requireActivity() as Navigator