package com.yelysei.hobbyharbor.screens.main.hobbydetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.model.UserHobbyIsNotLoadedException
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.screens.Configuration
import com.yelysei.hobbyharbor.screens.dialogs.ExperienceTimeDialog
import com.yelysei.hobbyharbor.screens.dialogs.OnExperienceTimeSubmitClickListener
import com.yelysei.hobbyharbor.screens.dialogs.SetGoalDialog
import com.yelysei.hobbyharbor.screens.dialogs.prepareDialog
import com.yelysei.hobbyharbor.screens.recyclerViewConfigureView
import com.yelysei.hobbyharbor.screens.renderSimpleResult
import com.yelysei.hobbyharbor.utils.AttributeUtils
import com.yelysei.hobbyharbor.utils.CustomTypeface
import com.yelysei.hobbyharbor.utils.appear
import com.yelysei.hobbyharbor.utils.setMovableBehavior
import com.yelysei.hobbyharbor.utils.viewModelCreator

class UserHobbyDetailsFragment : Fragment() {
    private lateinit var binding: FragmentUserHobbyDetailsBinding

    private val args: UserHobbyDetailsFragmentArgs by navArgs()

    private val viewModel by viewModelCreator {
        UserHobbyDetailsViewModel(
            args.uhId,
            Repositories.userHobbiesRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserHobbyDetailsBinding.inflate(inflater, container, false)
        val adapter = UserActionsAdapter(object : HobbyDetailsActionListener {
            override fun editAction(userAction: Action) {
                val dialogTitle = "Edit your experience:"
                val submitClickListener = { till: Long, from: Long ->
                    try {
                        viewModel.editUserExperience(till, from, userAction.id)
                        Toast.makeText(
                            context,
                            "User Experience has been changed",
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: UserHobbyIsNotLoadedException) {
                        Toast.makeText(
                            context,
                            "The error occurred while trying to add new experience",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                val previousFrom = userAction.startTime
                val previousTill = userAction.endTime
                showExperienceTimeDialog(
                    dialogTitle,
                    submitClickListener,
                    previousFrom,
                    previousTill
                )
            }
        })
        binding.recyclerViewUserActions.adapter = adapter

        viewModel.userHobby.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result) { userHobby ->
                binding.tvGoalValue.text = userHobby.progress.goal.toString()
                binding.tvTotalValue.text = userHobby.getProgressInHours().toString()
                adapter.userActions = userHobby.progress.actions

                binding.buttonEditGoal.setOnClickListener {
                    val setGoalDialog: SetGoalDialog = prepareDialog(userHobby.progress.goal) {
                        viewModel.updateProgress(it)
                    }
                    setGoalDialog.show()
                }
            }
        }


        recyclerViewConfigureView(
            Configuration(
                recyclerView = binding.recyclerViewUserActions,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 64,
            )
        )

        binding.buttonAddExperience.setMovableBehavior()
        val attributeUtils = AttributeUtils(binding.root, R.styleable.FabView)
        val fabColorStateList =
            ColorStateList.valueOf(attributeUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        attributeUtils.onClear()
        binding.buttonAddExperience.appear(fabColorStateList)
        binding.buttonAddExperience.setOnClickListener {
            onAddExperienceClick()
        }

        return binding.root
    }

    private fun onAddExperienceClick() {
        val dialogTitle = "Add new experience:"
        val submitClickListener: OnExperienceTimeSubmitClickListener = { from: Long, till: Long ->
            try {
                viewModel.addUserExperience(from, till)
                Toast.makeText(context, "New experience has been added", Toast.LENGTH_SHORT).show()
            } catch (e: UserHobbyIsNotLoadedException) {
                Toast.makeText(
                    context,
                    "The error occurred while trying to add new experience",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        showExperienceTimeDialog(dialogTitle, submitClickListener)
    }

    private fun showExperienceTimeDialog(
        title: String,
        submitClickListener: OnExperienceTimeSubmitClickListener,
        previousFrom: Long? = null,
        previousTill: Long? = null
    ) {
        val experienceTimeDialog = ExperienceTimeDialog(
            title,
            requireContext(),
            submitClickListener,
            parentFragmentManager
        )
        experienceTimeDialog.show()
        if (previousFrom != null && previousTill != null) {
            experienceTimeDialog.fulfillFromDateTime(previousFrom)
            experienceTimeDialog.fulfillTillDateTime(previousTill)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val hobbyName = arguments?.getString("hobbyName") ?: getString(R.string.app_name)
        requireActivity().findViewById<TextView>(R.id.toolbarTitle).text =
            CustomTypeface.capitalizeEachWord(hobbyName)
    }
}
