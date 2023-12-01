package com.yelysei.hobbyharbor.views.screens.base

import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.yelysei.hobbyharbor.App
import com.yelysei.hobbyharbor.navigator.ARG_SCREEN
import com.yelysei.hobbyharbor.navigator.MainNavigator
import com.yelysei.hobbyharbor.navigator.Navigator
import com.yelysei.hobbyharbor.service.UserHobbiesService

class ViewModelFactory(
    private val screen: BaseScreen,
    private val fragment: BaseFragment,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val hostActivity = fragment.requireActivity()
        val application = hostActivity.application as App
        val navigatorProvider = ViewModelProvider(hostActivity, ViewModelProvider.AndroidViewModelFactory(application))
        val navigator = navigatorProvider[MainNavigator::class.java]

        val constructor = modelClass.getConstructor(Navigator::class.java, screen::class.java, UserHobbiesService::class.java)
        return constructor.newInstance(navigator, screen, application.userHobbiesService)
    }

}

inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {
    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen
    ViewModelFactory(screen, this)
}