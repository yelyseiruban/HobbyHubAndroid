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
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.screens.renderSimpleResult
import com.yelysei.hobbyharbor.utils.viewModelCreator

class UserHobbyDetailsFragment: Fragment(){
    private lateinit var binding: FragmentUserHobbyDetailsBinding

    private val args: UserHobbyDetailsFragmentArgs by navArgs()

    private val viewModel by viewModelCreator { UserHobbyDetailsViewModel(args.uhId, Repositories.userHobbiesRepository) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentUserHobbyDetailsBinding.inflate(inflater, container, false)
        val adapter: UserActionsAdapter = UserActionsAdapter(object : HobbyDetailsActionListener {
            override fun editAction(userAction: Action) {
                TODO("Not yet implemented")
            }
        })


        viewModel.userHobby.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result) {
                binding.tvHobbyName.text = it.hobby.hobbyName
                binding.tvGoalValue.text = it.progress.goal.toString()
                binding.tvTotalValue.text = it.getProgressInHours().toString()
                adapter.userActions = it.progress.actions
            }
        }

        binding.recyclerViewUserActions.adapter = adapter



        binding.buttonNavigateUp.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }
}
