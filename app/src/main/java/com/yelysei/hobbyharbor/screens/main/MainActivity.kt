package com.yelysei.hobbyharbor.screens.main

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        Repositories.init(applicationContext)
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        supportActionBar?.setDisplayShowTitleEnabled(false)

        //prepare root nav controller
        val navController = getRootNavController()
        onNavControllerActivated(navController)

        binding.buttonNavigationUp.setOnClickListener {
            onSupportNavigateUp()
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.buttonNavigationUp.visibility = if (destination.id == R.id.userHobbiesFragment) {
                View.GONE
            } else {
                View.VISIBLE
            }
            binding.toolbarTitle.text = destination.label ?: "HobbyHarbor"

            if (destination.id == R.id.userHobbyDetailsFragment) {
                binding.buttonSettings.visibility = View.GONE
                binding.buttonUserProfile.visibility = View.GONE
                binding.buttonNotification.visibility = View.VISIBLE
            } else {
                binding.buttonSettings.visibility = View.VISIBLE
                binding.buttonUserProfile.visibility = View.VISIBLE
                binding.buttonNotification.visibility = View.GONE
            }
        }
    }

    override fun onDestroy() {
        navController = null
        super.onDestroy()
    }

    override fun onSupportNavigateUp(): Boolean = (navController?.navigateUp() ?: false) || super.onSupportNavigateUp()

    private fun onNavControllerActivated(navController: NavController) {
        if(this.navController == navController) return
        this.navController = navController
    }

    private fun getRootNavController(): NavController {
        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        return navHost.navController
    }

}