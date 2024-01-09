package com.yelysei.hobbyharbor.ui.screens.main.hobbydetails

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.model.UserHobbyIsNotLoadedException
import com.yelysei.hobbyharbor.model.userhobbies.entities.Experience
import com.yelysei.hobbyharbor.model.userhobbies.entities.getProgressInHours
import com.yelysei.hobbyharbor.ui.dialogs.OnExperienceTimeSubmitClickListener
import com.yelysei.hobbyharbor.ui.dialogs.SetGoalDialog.ProgressState
import com.yelysei.hobbyharbor.ui.dialogs.prepareDialog
import com.yelysei.hobbyharbor.ui.dialogs.showExperienceDialogs
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
        val hobbyName = args.hobbyName
        binding.toolbarTitle.text = CustomTypeface.capitalizeEachWord(hobbyName)
        val adapter = UserExperiencesAdapter(object : HobbyDetailsActionListener {
            override fun openExperience(experienceId: Int) {
                findNavController().navigate(
                    UserHobbyDetailsFragmentDirections.actionUserHobbyDetailsFragmentToExperienceDetailsFragment(
                        experienceId,
                        args.hobbyName
                    )
                )
            }

            override fun editExperience(experience: Experience) {
                onEditExperienceClick(experience)
            }
        })
        binding.recyclerViewUserExperiences.adapter = adapter

        viewModel.userHobby.observe(viewLifecycleOwner) { result ->
            com.yelysei.hobbyharbor.ui.screens.renderSimpleResult(
                binding.root,
                result
            ) { userHobby ->

                if (userHobby.getProgressInHours() >= userHobby.progress.goal) {
                    uiActions.toast(stringResources.getString(R.string.reached_goal_message))
                }

                binding.tvGoalValue.text = userHobby.progress.goal.toString()
                binding.tvTotalValue.text = userHobby.getProgressInHours().toString()
                adapter.userExperiences = userHobby.progress.experiences

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
                recyclerView = binding.recyclerViewUserExperiences,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 64,
            )
        )

        binding.buttonAddExperience.setMovableBehavior()
        val attributeUtils = AttributeUtils(binding.root, R.styleable.FabView)
        val fabColorStateList =
            ColorStateList.valueOf(attributeUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        attributeUtils.onClear()
        binding.buttonAddExperience.supportImageTintList = fabColorStateList
        binding.buttonAddExperience.setOnClickListener {
            onAddExperienceClick()
        }

        binding.buttonNavigationUp.setOnClickListener {
            findNavController().navigateUp()
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
        showExperienceDialogs(dialogTitle, submitClickListener)
    }

    private fun onEditExperienceClick(experience: Experience) {
        val dialogTitle = "Edit your experience:"
        val submitClickListener = { till: Long, from: Long ->
            try {
                viewModel.editUserExperience(till, from, experience.id)
                uiActions.toast(stringResources.getString(R.string.change_user_experience_confirmation))
            } catch (e: UserHobbyIsNotLoadedException) {
                uiActions.toast(stringResources.getString(R.string.change_user_experience_error))
            }
        }
        val previousFrom = experience.startTime
        val previousTill = experience.endTime
        showExperienceDialogs(
            dialogTitle,
            submitClickListener,
            previousFrom,
            previousTill
        )
    }

}
