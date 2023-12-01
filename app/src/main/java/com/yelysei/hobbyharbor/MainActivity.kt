 package com.yelysei.hobbyharbor

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yelysei.hobbyharbor.databinding.ActivityMainBinding
import com.yelysei.hobbyharbor.views.screens.userhobbies.UserHobbiesFragment

 class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fragment = UserHobbiesFragment()
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, fragment)
                .commit()
        }
    }

 }