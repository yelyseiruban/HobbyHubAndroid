package com.yelysei.hobbyharbor.screens.main.hobbydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.model.UserHobbyIsNotLoadedException
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.results.takeSuccess
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.screens.Configuration
import com.yelysei.hobbyharbor.screens.dialogs.ExperienceTimeDialog
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
        val adapter = UserActionsAdapter(object : HobbyDetailsActionListener {
            override fun editAction(userAction: Action) {
                val submitClickListener = {till: Long, from: Long ->
                    try {
                        viewModel.editUserExperience(till, from, userAction.id)
                        Toast.makeText(context, "User Experience has been changed", Toast.LENGTH_SHORT).show()
                    } catch (e: UserHobbyIsNotLoadedException) {
                        Toast.makeText(context, "The error occurred while trying to add new experience", Toast.LENGTH_SHORT).show()
                    }
                }
                val previousFrom = userAction.startTime
                val previousTill = userAction.endTime
                showExperienceTimeDialog(submitClickListener, previousFrom, previousTill)
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
        val submitClickListener: SubmitClickListener = {from: Long, till: Long ->
            try {
                viewModel.addUserExperience(from, till)
                Toast.makeText(context, "New experience has been added", Toast.LENGTH_SHORT).show()
            } catch (e: UserHobbyIsNotLoadedException) {
                Toast.makeText(context, "The error occurred while trying to add new experience", Toast.LENGTH_SHORT).show()
            }
        }
        showExperienceTimeDialog(submitClickListener)
    }

    private fun showExperienceTimeDialog(submitClickListener: SubmitClickListener, previousFrom: Long? = null, previousTill: Long? = null) {
        val experienceTimeDialog = ExperienceTimeDialog(requireContext(), submitClickListener, parentFragmentManager)
        experienceTimeDialog.show()
        if (previousFrom != null && previousTill != null) {
            experienceTimeDialog.fulfillFromDateTime(previousFrom)
            experienceTimeDialog.fulfillTillDateTime(previousTill)
        }
    }

}
