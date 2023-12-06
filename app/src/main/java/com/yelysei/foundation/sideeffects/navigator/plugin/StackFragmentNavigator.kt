package com.yelysei.foundation.sideeffects.navigator.plugin

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.annotation.AnimRes
import androidx.annotation.IdRes
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import com.yelysei.foundation.ARG_SCREEN
import com.yelysei.foundation.sideeffects.SideEffectImplementation
import com.yelysei.foundation.sideeffects.navigator.Navigator
import com.yelysei.foundation.utils.Event
import com.yelysei.foundation.views.BaseFragment
import com.yelysei.foundation.views.BaseScreen

class StackFragmentNavigator(
    @IdRes private val containerId: Int,
    private val animations: Animations,
    private val initialScreenCreator: () -> BaseScreen
) : SideEffectImplementation(), Navigator, LifecycleObserver {

    private var result: Event<Any>? = null
    override fun launch(screen: BaseScreen) {
        launchFragment(screen)
    }

    override fun goBack(result: Any?) {
        if (result !== null) {
            this.result = Event(result)
        }
        requireActivity().onBackPressedDispatcher.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        requireActivity().lifecycle.addObserver(this)
        if (savedInstanceState == null) {
            launchFragment(
                screen = initialScreenCreator(),
                addToBackStack = false
            )
        }
        requireActivity().supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }


    override fun onBackPressed(): Boolean {
        val fragment = requireActivity().supportFragmentManager.findFragmentById(containerId)
        return if (fragment is BaseFragment) {
            Log.d("Debug", "fragment.viewModel.onBackPressed()")
            fragment.viewModel.onBackPressed()
        } else {
            false
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(){
        requireActivity().supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
    }

    fun launchFragment(screen: BaseScreen, addToBackStack: Boolean = true) {
        val fragment = screen.javaClass.enclosingClass.getDeclaredConstructor().newInstance() as Fragment
        fragment.arguments = bundleOf(ARG_SCREEN to screen)
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        if (addToBackStack) transaction.addToBackStack(null)
        transaction
            .setCustomAnimations(
                animations.enterAnim,
                animations.exitAnim,
                animations.popEnterAnim,
                animations.popExitAnim
            )
            .replace(containerId, fragment)
            .commit()
    }

    private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            val result = result?.getValue() ?: return
            if (f is BaseFragment) {
                f.viewModel.onResult(result)
            }
        }
    }

    class Animations(
        @AnimRes val enterAnim: Int,
        @AnimRes val exitAnim: Int,
        @AnimRes val popEnterAnim: Int,
        @AnimRes val popExitAnim: Int
    )
}