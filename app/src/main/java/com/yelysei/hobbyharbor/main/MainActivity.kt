 package com.yelysei.hobbyharbor.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.views.HasScreenTitle
import com.yelysei.hobbyharbor.views.userhobbies.UserHobbiesFragment

 class MainActivity : AppCompatActivity() {
     private val activityViewModel by viewModels<MainViewModel> { ViewModelProvider.AndroidViewModelFactory(application) }

     private lateinit var toolbar: Toolbar
     private lateinit var toolbarTitle: TextView
     private lateinit var ibSettings: ImageButton
     private lateinit var ibUserProfile: ImageButton
     override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false) // Hide default title
        toolbarTitle = findViewById(R.id.toolbar_title)
        ibSettings = findViewById(R.id.buttonSettings)
        ibUserProfile = findViewById(R.id.buttonUserProfile)

        ibSettings.setOnClickListener {
            activityViewModel.onSettingsPressed()
        }

        ibUserProfile.setOnClickListener {
            activityViewModel.onUserProfilePressed()
        }

        if (savedInstanceState == null) {
            activityViewModel.launchFragment(this, UserHobbiesFragment.Screen(), addToBackStack = false)
        }

        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentCallbacks, false)
    }

     override fun onDestroy() {
         supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentCallbacks)
         super.onDestroy()
     }

     private fun setToolbarTitle(title: String) {
         toolbarTitle.text = title
     }

     override fun onSupportNavigateUp(): Boolean {
         onBackPressedDispatcher.onBackPressed()
         return true
     }

     override fun onResume() {
         super.onResume()
         activityViewModel.whenActivityActive.resource = this
     }

     override fun onPause() {
         super.onPause()
         activityViewModel.whenActivityActive.resource = null
     }

     fun notifyScreenUpdates() {
         val f = supportFragmentManager.findFragmentById(R.id.fragmentContainer)

         if (f is HasScreenTitle && f.getScreenTitle() != null) {
             setToolbarTitle(f.getScreenTitle().toString())
         } else {
             supportActionBar?.title = "Hobby Harbor"
         }

     }

     private val fragmentCallbacks = object : FragmentManager.FragmentLifecycleCallbacks() {
         override fun onFragmentViewCreated(
             fm: FragmentManager,
             f: Fragment,
             v: View,
             savedInstanceState: Bundle?
         ) {
             notifyScreenUpdates()
         }
     }

 }
