package com.yelysei.foundation.views

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.yelysei.foundation.ARG_SCREEN
import com.yelysei.foundation.SingletonScopeDependencies
import com.yelysei.foundation.views.activity.ActivityDelegateHolder
import java.lang.reflect.Constructor

inline fun <reified VM : ViewModel> BaseFragment.screenViewModel() = viewModels<VM> {

    val application = requireActivity().application

    val screen = requireArguments().getSerializable(ARG_SCREEN) as BaseScreen

    val activityScopeViewModel = (requireActivity() as ActivityDelegateHolder).delegate.getActivityScopeViewModel()

    val dependencies = listOf(screen) + activityScopeViewModel.sideEffectMediators + SingletonScopeDependencies.getSingletonScopeDependencies(application)

    ViewModelFactory(dependencies, this)
}

class ViewModelFactory(
    private val dependencies: List<Any>,
    owner: SavedStateRegistryOwner
) : AbstractSavedStateViewModelFactory(owner, null) {

    override fun <T : ViewModel> create(
        key: String,
        modelClass: Class<T>,
        handle: SavedStateHandle
    ): T {
        val constructors = modelClass.constructors
        val constructor = constructors.maxByOrNull { it.typeParameters.size }!!
        val dependenciesWithSavedState = dependencies + handle
        val arguments = findDependencies(constructor, dependenciesWithSavedState)
        return constructor.newInstance(*arguments.toTypedArray()) as T
    }

    private fun findDependencies(constructor: Constructor<*>, dependencies: List<Any>) : List<Any> {
        val args = mutableListOf<Any>()
        constructor.parameterTypes.forEach { parameterClass ->
            Log.d("findDependences", dependencies.toString())
            Log.d("parameterClass", parameterClass.toString())
            dependencies.forEach {
                Log.d("dependency", parameterClass.isAssignableFrom(it.javaClass).toString())
            }
            val dependency = dependencies.first { parameterClass.isAssignableFrom(it.javaClass) }
            args.add(dependency)
        }
        return args
    }

}