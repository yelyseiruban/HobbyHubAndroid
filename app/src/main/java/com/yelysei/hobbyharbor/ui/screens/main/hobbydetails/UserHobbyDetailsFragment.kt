package com.yelysei.hobbyharbor.ui.screens.main.hobbydetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.model.UserHobbyIsNotLoadedException
import com.yelysei.hobbyharbor.model.userhobbies.entities.Action
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.ui.dialogs.ExperienceTimeDialog
import com.yelysei.hobbyharbor.ui.dialogs.OnExperienceTimeSubmitClickListener
import com.yelysei.hobbyharbor.ui.dialogs.SetGoalDialog.ProgressState
import com.yelysei.hobbyharbor.ui.dialogs.prepareDialog
import com.yelysei.hobbyharbor.ui.fab.appear
import com.yelysei.hobbyharbor.ui.fab.setMovableBehavior
import com.yelysei.hobbyharbor.ui.screens.main.BaseFragment
import com.yelysei.hobbyharbor.utils.CustomTypeface
import com.yelysei.hobbyharbor.utils.resources.AttributeUtils
import com.yelysei.hobbyharbor.utils.viewModelCreator

class UserHobbyDetailsFragment : BaseFragment() {
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
                        uiActions.toast(stringResources.getString(R.string.change_user_experience_confirmation))
                    } catch (e: UserHobbyIsNotLoadedException) {
                        uiActions.toast(stringResources.getString(R.string.change_user_experience_error))
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
            com.yelysei.hobbyharbor.ui.screens.renderSimpleResult(
                binding.root,
                result
            ) { userHobby ->
                binding.tvGoalValue.text = userHobby.progress.goal.toString()
                binding.tvTotalValue.text = userHobby.getProgressInHours().toString()
                adapter.userActions = userHobby.progress.actions

                binding.buttonEditGoal.setOnClickListener {
                    val setGoalDialog = prepareDialog(
                        onSubmitClickListener = {
                            viewModel.updateProgress(it)
                        },
                        progressState = ProgressState(
                            userHobby.progress.goal,
                            userHobby.getProgressInHours().toInt()
                        )
                    )
                    setGoalDialog.show()
                }
            }
            }



        com.yelysei.hobbyharbor.ui.screens.recyclerViewConfigureView(
            com.yelysei.hobbyharbor.ui.screens.Configuration(
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
                uiActions.toast(stringResources.getString(R.string.experience_added_confirmation))
            } catch (e: UserHobbyIsNotLoadedException) {
                uiActions.toast(stringResources.getString(R.string.experience_added_error))
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
            uiActions,
            stringResources,
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
