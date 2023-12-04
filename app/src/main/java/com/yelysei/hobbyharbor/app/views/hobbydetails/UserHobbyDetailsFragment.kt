package com.yelysei.hobbyharbor.app.views.hobbydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.foundation.views.BaseFragment
import com.yelysei.foundation.views.BaseScreen
import com.yelysei.foundation.views.VerticalSpaceItemDecoration
import com.yelysei.foundation.views.screenViewModel
import com.yelysei.hobbyharbor.app.model.userhobbies.Action
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding

class UserHobbyDetailsFragment: BaseFragment(){

    override val viewModel by screenViewModel<UserHobbyDetailsViewModel>()

    class Screen(
        val uhId: Long
    ) : BaseScreen

    private lateinit var binding: FragmentUserHobbyDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserHobbyDetailsBinding.inflate(layoutInflater, container, false)

        val adapter = UserActionsAdapter(object : HobbyDetailsActionListener {
            override fun editAction(userAction: Action) {
                viewModel.onEditAction()
            }
        })

        viewModel.userHobby.observe(viewLifecycleOwner) {
            with(binding) {
                tvHobbyName.text = it.hobby.hobbyName
                tvTotalValue.text = it.progress.currentProgress.toString()
                tvGoalValue.text = it.progress.goal.toString()
                adapter.userActions = it.progress.history
            }
        }

        viewModel

        //recycler view config space between items
        val layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewUserActions.layoutManager = layoutManager
        binding.recyclerViewUserActions.addItemDecoration(
            VerticalSpaceItemDecoration(
                ACTIONS_SPACE_BETWEEN
            )
        )
        binding.recyclerViewUserActions.adapter = adapter

        binding.buttonEditGoal.setOnClickListener {
            viewModel.onGoalEdit()
        }

        binding.buttonAddExperience.setOnClickListener{
            viewModel.onAddExperience()
        }

        binding.buttonNotification.setOnClickListener {
            viewModel.onNotificationPressed()
        }

        binding.buttonBack.setOnClickListener {
            viewModel.onBackPressed()
        }

        return binding.root
    }

    companion object {
        private const val ACTIONS_SPACE_BETWEEN = 40
    }
}