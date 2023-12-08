package com.yelysei.hobbyharbor.app.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.yelysei.hobbyharbor.R

//class MainActivity : BaseActivity() {
//
//    override fun registerPlugins(manager: SideEffectPluginsManager) = with (manager) {
//        val navigator = createNavigator()
//        register(ToastsPlugin())
//        register(ResourcesPlugin())
//        register(NavigatorPlugin(navigator))
//        register(PermissionsPlugin())
//        register(DialogsPlugin())
//        register(IntentsPlugin())
//    }
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        Log.d("init dep", "before init dep")
//        Initializer.initDependencies()
//        Log.d("init dep", "after init dep")
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        val binding = ActivityMainBinding.inflate(layoutInflater)
//        val navigator = createNavigator()
//
//        with(binding) {
//            setSupportActionBar(toolbar)
//
//            buttonSettings.setOnClickListener {
////                navigator.launchFragment(SettingsFragment.Screen())
//            }
//
//            buttonUserProfile.setOnClickListener {
////                navigator.launchFragment(UserProfileFragment.Screen())
//            }
//        }
//
//        supportActionBar?.setDisplayShowTitleEnabled(false)
//    }
//    private fun createNavigator() = StackFragmentNavigator(
//        containerId = R.id.fragmentContainer,
//        StackFragmentNavigator.Animations(
//            R.anim.enter,
//            R.anim.exit,
//            R.anim.pop_enter,
//            R.anim.pop_exit
//        ),
//        initialScreenCreator = { UserHobbiesFragment.Screen() }
//    )
//}

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}