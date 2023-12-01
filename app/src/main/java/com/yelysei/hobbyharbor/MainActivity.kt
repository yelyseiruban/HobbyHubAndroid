 package com.yelysei.hobbyharbor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.yelysei.hobbyharbor.navigator.MainNavigator
import com.yelysei.hobbyharbor.views.screens.userhobbies.UserHobbiesFragment

 class MainActivity : AppCompatActivity() {
     private val navigator by viewModels<MainNavigator> { ViewModelProvider.AndroidViewModelFactory(application) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            navigator.launchFragment(this, UserHobbiesFragment.Screen(), addToBackStack = false)
        }
        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

     override fun onDestroy() {
         super.onDestroy()
         supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
     }

     override fun onSupportNavigateUp(): Boolean {
         onBackPressedDispatcher.onBackPressed()
         return true
     }

     override fun onResume() {
         super.onResume()
         navigator.whenActivityActive.mainActivity = this
     }

     override fun onPause() {
         super.onPause()
         navigator.whenActivityActive.mainActivity = null
     }

     private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
         override fun onFragmentViewCreated(
             fm: FragmentManager,
             f: Fragment,
             v: View,
             savedInstanceState: Bundle?
         ) {
             if (supportFragmentManager.backStackEntryCount > 0) {
                 supportActionBar?.setDisplayHomeAsUpEnabled(true)
             } else {
                 supportActionBar?.setDisplayHomeAsUpEnabled(false)
             }
         }
     }
 }
