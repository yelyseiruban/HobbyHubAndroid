package com.yelysei.hobbyharbor.views.hobbydetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbyDetailsBinding
import com.yelysei.hobbyharbor.model.hobbies.Action
import com.yelysei.hobbyharbor.views.VerticalSpaceItemDecoration
import com.yelysei.hobbyharbor.views.base.BaseFragment
import com.yelysei.hobbyharbor.views.base.BaseScreen
import com.yelysei.hobbyharbor.views.base.screenViewModel

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