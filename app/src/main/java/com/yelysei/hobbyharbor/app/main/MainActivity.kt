package com.yelysei.hobbyharbor.app.main

import android.os.Bundle
import com.yelysei.foundation.sideeffects.SideEffectPluginsManager
import com.yelysei.foundation.sideeffects.dialogs.plugin.DialogsPlugin
import com.yelysei.foundation.sideeffects.intents.plugin.IntentsPlugin
import com.yelysei.foundation.sideeffects.navigator.plugin.NavigatorPlugin
import com.yelysei.foundation.sideeffects.navigator.plugin.StackFragmentNavigator
import com.yelysei.foundation.sideeffects.permissions.plugin.PermissionsPlugin
import com.yelysei.foundation.sideeffects.resources.plugin.ResourcesPlugin
import com.yelysei.foundation.sideeffects.toasts.plugin.ToastsPlugin
import com.yelysei.foundation.views.activity.BaseActivity
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.app.views.userhobbies.UserHobbiesFragment
import com.yelysei.hobbyharbor.databinding.ActivityMainBinding

class MainActivity : BaseActivity() {

    override fun registerPlugins(manager: SideEffectPluginsManager) = with (manager) {
        val navigator = createNavigator()
        register(ToastsPlugin())
        register(ResourcesPlugin())
        register(NavigatorPlugin(navigator))
        register(PermissionsPlugin())
        register(DialogsPlugin())
        register(IntentsPlugin())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        val navigator = createNavigator()

        with(binding) {
            setSupportActionBar(toolbar)

            buttonSettings.setOnClickListener {
//                navigator.launchFragment(SettingsFragment.Screen())
            }

            buttonUserProfile.setOnClickListener {
//                navigator.launchFragment(UserProfileFragment.Screen())
            }
        }

        supportActionBar?.setDisplayShowTitleEnabled(false)
    }
    private fun createNavigator() = StackFragmentNavigator(
        containerId = R.id.fragmentContainer,
        StackFragmentNavigator.Animations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        ),
        initialScreenCreator = { UserHobbiesFragment.Screen() }
    )
}
