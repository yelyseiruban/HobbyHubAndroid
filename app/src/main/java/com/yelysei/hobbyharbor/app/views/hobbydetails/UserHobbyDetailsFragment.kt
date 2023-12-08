package com.yelysei.hobbyharbor.app.views.hobbydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding

class UserHobbyDetailsFragment: Fragment(){
    private lateinit var binding: FragmentUserHobbyDetailsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserHobbyDetailsBinding.inflate(inflater, container, false)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }
}