package com.yelysei.hobbyharbor.screens.main.categorizedhobbies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories.hobbiesRepository
import com.yelysei.hobbyharbor.Repositories.userHobbiesRepository
import com.yelysei.hobbyharbor.databinding.FragmentCategorizedHobbiesBinding
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.screens.Configuration
import com.yelysei.hobbyharbor.screens.dialogs.SetGoalDialog
import com.yelysei.hobbyharbor.screens.dialogs.prepareDialog
import com.yelysei.hobbyharbor.screens.onTryAgain
import com.yelysei.hobbyharbor.screens.recyclerViewConfigureView
import com.yelysei.hobbyharbor.screens.renderSimpleResult
import com.yelysei.hobbyharbor.screens.uiactions.UiActionsImpl
import com.yelysei.hobbyharbor.utils.viewModelCreator

class CategorizedHobbiesFragment : Fragment() {
    private val args: CategorizedHobbiesFragmentArgs by navArgs()
    private val viewModel by viewModelCreator { CategorizedHobbiesViewModel(args.categoryName, hobbiesRepository, userHobbiesRepository, UiActionsImpl(context)) }
    private lateinit var binding: FragmentCategorizedHobbiesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategorizedHobbiesBinding.inflate(inflater, container, false)

        binding.tvCategoryNameTitle.text = args.categoryName

        val adapter = HobbiesAdapter {
            showAddHobbyDialog(it)
        }

        viewModel.categorizedHobbies.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result, onSuccess = {
                adapter.hobbies = it
            })
        }
        binding.recyclerViewCategorizedHobbies.adapter = adapter
        recyclerViewConfigureView(Configuration(
            recyclerView = binding.recyclerViewCategorizedHobbies,
            layoutManager = LinearLayoutManager(requireContext()),
            verticalItemSpace = 64,
            constraintLayout = binding.constraintLayout,
            maxHeight = 0.65f
        ))


        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        binding.buttonNavigateUp.setOnClickListener {
            findNavController().navigateUp()
        }

        return binding.root
    }

    private fun showAddHobbyDialog(hobby: Hobby) {
        val setGoalDialog: SetGoalDialog = prepareDialog()
        setGoalDialog.show {
            viewModel.addUserHobby(hobby, it)
            findNavController().navigate(R.id.userHobbiesFragment)
        }
    }
}