package com.yelysei.hobbyharbor.app.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yelysei.foundation.ActivityScopeViewModel
import com.yelysei.foundation.navigator.IntermediateNavigator
import com.yelysei.foundation.navigator.StackFragmentNavigator
import com.yelysei.foundation.uiactions.AndroidUiActions
import com.yelysei.foundation.utils.viewModelCreator
import com.yelysei.foundation.views.FragmentsHolder
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.app.views.userhobbies.UserHobbiesFragment
import com.yelysei.hobbyharbor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), FragmentsHolder {

    private lateinit var navigator: StackFragmentNavigator

    private val activityViewModel by viewModelCreator<ActivityScopeViewModel> {
        ActivityScopeViewModel(
            uiActions = AndroidUiActions(applicationContext),
            navigator = IntermediateNavigator()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navigator = StackFragmentNavigator(
            activity = this,
            containerId = R.id.fragmentContainer,
            StackFragmentNavigator.Animations(
                R.anim.enter,
                R.anim.exit,
                R.anim.pop_enter,
                R.anim.pop_exit
            ),
            initialScreenCreator = { UserHobbiesFragment.Screen() }
        )
        navigator.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)

        with(binding) {
            setSupportActionBar(toolbar)

            buttonSettings.setOnClickListener {
                activityViewModel.onSettingsPressed()
            }

            buttonUserProfile.setOnClickListener {
                activityViewModel.onUserProfilePressed()
            }
        }

        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onDestroy() {
        activityViewModel.navigator.clear()
        navigator.onDestroy()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        activityViewModel.navigator.setTarget(navigator)
    }

    override fun onPause() {
        activityViewModel.navigator.setTarget(null)
        super.onPause()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }

    override fun getActivityScopeViewModel(): ActivityScopeViewModel {
        return activityViewModel
    }
}
