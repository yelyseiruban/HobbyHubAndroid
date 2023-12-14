package com.yelysei.hobbyharbor.screens.main.hobbydetails

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.results.takeSuccess
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.screens.Configuration
import com.yelysei.hobbyharbor.screens.dialogs.AddExperienceDialog
import com.yelysei.hobbyharbor.screens.dialogs.Date
import com.yelysei.hobbyharbor.screens.dialogs.SubmitClickListener
import com.yelysei.hobbyharbor.screens.recyclerViewConfigureView
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
        binding.recyclerViewUserActions.adapter = adapter

        viewModel.userHobby.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result) {
                binding.tvHobbyName.text = it.hobby.hobbyName
                binding.tvGoalValue.text = it.progress.goal.toString()
                binding.tvTotalValue.text = it.getProgressInHours().toString()
            }
        }

        viewModel.actions.observe(viewLifecycleOwner) { result ->
            if (result is SuccessResult) {
                Log.d("user hobby details fragmnet", result.takeSuccess().toString())
                adapter.userActions = result.takeSuccess() ?: listOf()

            }
        }

        recyclerViewConfigureView(
            Configuration(
                recyclerView = binding.recyclerViewUserActions,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 64,
                constraintLayout = binding.constraintLayout,
                maxHeight = 0.4f
            )
        )

        binding.buttonAddExperience.setOnClickListener {
            onAddExperienceClick()
        }

        binding.buttonNavigateUp.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun onAddExperienceClick() {
        val submitClickListener: SubmitClickListener = {from: Date, till: Date ->
            viewModel.addUserExperience(from, till)
        }
        val addExperienceDialog = AddExperienceDialog(requireContext(), submitClickListener)
        addExperienceDialog.show()
        findNavController()
    }

}
