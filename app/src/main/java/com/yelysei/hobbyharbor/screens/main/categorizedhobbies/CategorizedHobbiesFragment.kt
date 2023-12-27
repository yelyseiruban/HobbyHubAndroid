package com.yelysei.hobbyharbor.screens.main.categorizedhobbies

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories.hobbiesRepository
import com.yelysei.hobbyharbor.Repositories.userHobbiesRepository
import com.yelysei.hobbyharbor.databinding.FragmentCategorizedHobbiesBinding
import com.yelysei.hobbyharbor.model.hobbies.entities.Hobby
import com.yelysei.hobbyharbor.model.results.SuccessResult
import com.yelysei.hobbyharbor.model.results.takeSuccess
import com.yelysei.hobbyharbor.screens.Configuration
import com.yelysei.hobbyharbor.screens.dialogs.AddCustomHobbyDialog
import com.yelysei.hobbyharbor.screens.dialogs.SetGoalDialog
import com.yelysei.hobbyharbor.screens.dialogs.prepareDialog
import com.yelysei.hobbyharbor.screens.onTryAgain
import com.yelysei.hobbyharbor.screens.recyclerViewConfigureView
import com.yelysei.hobbyharbor.screens.renderSimpleResult
import com.yelysei.hobbyharbor.screens.uiactions.UiActions
import com.yelysei.hobbyharbor.screens.uiactions.UiActionsImpl
import com.yelysei.hobbyharbor.utils.SearchBarOnTextChangeListener
import com.yelysei.hobbyharbor.utils.viewModelCreator

class CategorizedHobbiesFragment : Fragment() {
    private val viewModel by viewModelCreator { CategorizedHobbiesViewModel(hobbiesRepository, userHobbiesRepository, UiActionsImpl(context)) }
    private lateinit var binding: FragmentCategorizedHobbiesBinding
    private lateinit var addCustomHobbyDialog: AddCustomHobbyDialog
    private val uiActions: UiActions by lazy {
        UiActionsImpl(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategorizedHobbiesBinding.inflate(inflater, container, false)

        binding.searchView.editText
            .setOnEditorActionListener { _, _, _ ->
                return@setOnEditorActionListener false
            }

        val availableHobbiesAdapter = setAdapter()
        val searchedHobbiesAdapter = setAdapter()

        searchedHobbiesAdapter.processHobbies(listOf())

        viewModel.categorizedHobbies.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result, onSuccess = {
                availableHobbiesAdapter.processHobbies(it)
            })
        }

        viewModel.searchedHobbies.observe(viewLifecycleOwner) { result ->
            if (result is SuccessResult) {
                searchedHobbiesAdapter.processHobbies(result.data)
            }
        }


        binding.searchView.editText.addTextChangedListener(object: SearchBarOnTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchHobbies(s.toString())
                binding.searchView.editText.requestFocus()
            }
        })

        binding.recyclerViewAvailableCategorizedHobbies.adapter = availableHobbiesAdapter
        binding.recyclerViewAvailableCategorizedHobbies.layoutManager = LinearLayoutManager(requireContext())

        binding.recyclerViewSearchedCategorizedHobbies.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewSearchedCategorizedHobbies.adapter = searchedHobbiesAdapter
        binding.searchView.setupWithSearchBar(binding.searchBar)

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        configureAvailableHobbiesView()

        binding.buttonAddCustomHobby.setOnClickListener {
            val context = requireContext()
            addCustomHobbyDialog = AddCustomHobbyDialog(
                context,
                UiActionsImpl(context),
                viewModel.categories.value.takeSuccess()
                    ?: throw IllegalStateException("Categories have not been loaded")
            ) { hobby ->
                viewModel.checkIfHobbyExists(hobby.hobbyName)
                    .observe(viewLifecycleOwner) { hobbyExists ->
                        if (hobbyExists) {
                            uiActions.toast(context.getString(R.string.hobby_exists_exception, hobby.hobbyName))
                        } else {
                            addCustomHobbyDialog.dialog.dismiss()
                            Handler(Looper.getMainLooper()).postDelayed({
                                SetGoalDialog(context, null, uiActions) { goal ->
                                    viewModel.addCustomHobby(hobby, goal)
                                    findNavController().navigate(R.id.userHobbiesFragment)
                                }.show()
                            }, 200)
                        }
                    }
            }
            addCustomHobbyDialog.show()
        }

        return binding.root
    }

    private fun showAddHobbyDialog(hobby: Hobby) {
        if (!isAdded || isDetached) {
            return
        }
        viewModel.checkIfUserHobbyExists(hobby.id ?: throw Exception("Hobby does not have Id")).observe(viewLifecycleOwner) { hobbyExists ->
            if (hobbyExists) {
                uiActions.toast(requireContext().getString(R.string.hobby_exists_exception, hobby.hobbyName))
            } else {
                val setGoalDialog: SetGoalDialog = prepareDialog(onSubmitClickListener = {
                    viewModel.addUserHobby(hobby, it)
                    findNavController().navigate(R.id.userHobbiesFragment)
                })
                setGoalDialog.show()
            }
        }
    }

    private fun configureAvailableHobbiesView() {
        recyclerViewConfigureView( Configuration(
            recyclerView = binding.recyclerViewAvailableCategorizedHobbies,
            layoutManager = LinearLayoutManager(requireContext()),
            verticalItemSpace = 0,
            constraintLayout = binding.constraintLayout,
            maxHeight = 0.75f
            )
        )
    }

    private fun setAdapter() : HobbiesAdapter {
        return HobbiesAdapter(requireContext()) {
            showAddHobbyDialog(it)
        }
    }
}