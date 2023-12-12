package com.yelysei.hobbyharbor.screens.main.hobbydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.utils.viewModelCreator

class UserHobbyDetailsFragment: Fragment(){
    private lateinit var binding: FragmentUserHobbyDetailsBinding

    private val viewModel by viewModelCreator { UserHobbyDetailsViewModel(Repositories.userHobbiesRepository) }

    private val args: UserHobbyDetailsFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

//        viewModel.load(args.uhId)
        binding = FragmentUserHobbyDetailsBinding.inflate(inflater, container, false)

        binding.buttonNavigateUp.setOnClickListener {
            findNavController().navigateUp()
        }

//        binding.tvHobbyName.text = viewModel.getUserHobbyName()
//        binding.tvTotalValue.text = viewModel.getTotalValue().toString()
//        binding.tvGoalValue.text = viewModel.getGoalValue().toString()

        return binding.root
    }
}