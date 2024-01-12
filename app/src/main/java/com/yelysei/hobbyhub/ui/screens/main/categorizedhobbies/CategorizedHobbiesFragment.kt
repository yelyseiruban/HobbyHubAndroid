package com.yelysei.hobbyhub.ui.screens.main.categorizedhobbies

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyhub.R
import com.yelysei.hobbyhub.Repositories.hobbiesRepository
import com.yelysei.hobbyhub.Repositories.userHobbiesRepository
import com.yelysei.hobbyhub.databinding.FragmentCategorizedHobbiesBinding
import com.yelysei.hobbyhub.model.hobbies.entities.Hobby
import com.yelysei.hobbyhub.model.results.SuccessResult
import com.yelysei.hobbyhub.model.results.takeSuccess
import com.yelysei.hobbyhub.ui.dialogs.AddCustomHobbyDialog
import com.yelysei.hobbyhub.ui.dialogs.SetGoalDialog
import com.yelysei.hobbyhub.ui.dialogs.prepareDialog
import com.yelysei.hobbyhub.ui.fab.setMovableBehavior
import com.yelysei.hobbyhub.ui.screens.main.BaseFragment
import com.yelysei.hobbyhub.utils.SearchBarOnTextChangeListener
import com.yelysei.hobbyhub.utils.resources.AttributeUtils
import com.yelysei.hobbyhub.utils.viewModelCreator


class CategorizedHobbiesFragment : BaseFragment() {
    private val viewModel by viewModelCreator {
        CategorizedHobbiesViewModel(
            hobbiesRepository,
            userHobbiesRepository,
            uiActions,
            stringResources
        )
    }
    private lateinit var binding: FragmentCategorizedHobbiesBinding
    private lateinit var addCustomHobbyDialog: AddCustomHobbyDialog
    private var statusBarColorChanged: Boolean = false
    private var defaultStatusBarColor: Int = R.attr.defaultStatusBarBackground
    private var searchViewLikeStatusBarColor: Int = R.attr.searchViewLikeStatusBarBackground
    private var searchViewFocused: Boolean = false

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (searchViewFocused) {
                hideSearchView()
            } else {
                findNavController().popBackStack()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategorizedHobbiesBinding.inflate(inflater, container, false)

        setSearchColors()

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        val availableHobbiesAdapter = setAdapter()
        val searchedHobbiesAdapter = setAdapter()

        searchedHobbiesAdapter.processHobbies(listOf())

        viewModel.categorizedHobbies.observe(viewLifecycleOwner) { result ->
            com.yelysei.hobbyhub.ui.screens.renderSimpleResult(binding.root, result) {
                availableHobbiesAdapter.processHobbies(it)
            }
        }

        viewModel.searchedHobbies.observe(viewLifecycleOwner) { result ->
            if (result is SuccessResult) {
                searchedHobbiesAdapter.processHobbies(result.data)
            }
        }

        binding.searchView.toolbar.setNavigationOnClickListener {v ->
            hideSearchView()
        }

        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchBar.setOnClickListener {
            binding.searchView.show()
            viewModel.setSearchViewFocused(true)
        }

        binding.searchView.editText.addTextChangedListener(object :
            SearchBarOnTextChangeListener() {
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                viewModel.searchHobbies(s.toString())
                binding.searchView.editText.requestFocus()
            }
        })

        binding.recyclerViewAvailableCategorizedHobbies.adapter = availableHobbiesAdapter
        binding.recyclerViewAvailableCategorizedHobbies.layoutManager =
            LinearLayoutManager(requireContext())

        binding.recyclerViewSearchedCategorizedHobbies.layoutManager =
            LinearLayoutManager(requireContext())
        binding.recyclerViewSearchedCategorizedHobbies.adapter = searchedHobbiesAdapter

        com.yelysei.hobbyhub.ui.screens.onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        viewModel.searchViewFocused.observe(viewLifecycleOwner) {
            searchViewFocused = it
            if (it == true) {
                setStatusBarSearchViewLikeColor()
            } else {
                setStatusBarDefaultColor()
            }
        }

        configureHobbiesRecyclerViews()

        binding.buttonAddCustomHobby.setMovableBehavior()
        val attributeFABUtils = AttributeUtils(binding.root, R.styleable.FabView)
        val fabColorStateList =
            ColorStateList.valueOf(attributeFABUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        attributeFABUtils.onClear()

        val attributeStatusBarUtils = AttributeUtils(binding.root, R.styleable.StatusBar)
        defaultStatusBarColor = attributeStatusBarUtils.getColorFromAttribute(R.styleable.StatusBar_defaultStatusBarBackground)
        searchViewLikeStatusBarColor = attributeStatusBarUtils.getColorFromAttribute(R.styleable.StatusBar_searchViewLikeStatusBarBackground)
        attributeStatusBarUtils.onClear()

        binding.buttonAddCustomHobby.supportImageTintList = fabColorStateList
        binding.buttonAddCustomHobby.setOnClickListener {
            showAddCustomHobbyDialog()
        }

        return binding.root
    }

    private fun setSearchColors() {
        val attributeUtils = AttributeUtils(binding.root, R.styleable.SearchBar)
        val iconColor = attributeUtils.getColorFromAttribute(R.styleable.SearchBar_navigationIconColor)
        attributeUtils.onClear()
        binding.searchBar.navigationIcon?.setTint(iconColor)
    }

    private fun setStatusBarDefaultColor() {
        requireActivity().window.statusBarColor = defaultStatusBarColor
    }

    private fun setStatusBarSearchViewLikeColor() {
        requireActivity().window.statusBarColor = searchViewLikeStatusBarColor
    }

    override fun onPause() {
        super.onPause()
        setStatusBarDefaultColor()
    }

    override fun onResume() {
        super.onResume()
        viewModel.setSearchViewFocused(searchViewFocused)
    }


    private fun hideSearchView() {
        viewModel.setSearchViewFocused(false)
        binding.searchView.hide()
        setStatusBarDefaultColor()
        statusBarColorChanged = false
    }

    private fun showAddCustomHobbyDialog() {
        if (!isAdded || isDetached) {
            return
        }
        addCustomHobbyDialog = AddCustomHobbyDialog(
            requireContext(),
            uiActions,
            stringResources,
            viewModel.categories.value.takeSuccess()
                ?: throw IllegalStateException("Categories have not been loaded")
        ) { hobby ->
            val hobbyExists = viewModel.checkIfHobbyExists(hobby.hobbyName)
            if (hobbyExists) {
                uiActions.toast(
                    stringResources.getString(
                        R.string.hobby_exists_exception,
                        hobby.hobbyName
                    )
                )
            } else {
                addCustomHobbyDialog.dialog.dismiss()
                val setGoalDialog = prepareDialog(onSubmitClickListener = { goal ->
                    viewModel.addCustomHobby(hobby, goal)
                    findNavController().navigate(R.id.userHobbiesFragment)
                })
                setGoalDialog.show()
            }
        }
        addCustomHobbyDialog.show()
    }

    private fun showAddHobbyDialog(hobby: Hobby) {
        if (!isAdded || isDetached) {
            return
        }
        viewModel.checkIfUserHobbyExists(hobby.id ?: throw Exception("Hobby does not have Id"))
            .observe(viewLifecycleOwner) { hobbyExists ->
                if (hobbyExists) {
                    uiActions.toast(
                        stringResources.getString(
                            R.string.hobby_exists_exception,
                            hobby.hobbyName
                        )
                    )
                } else {
                    val setGoalDialog: SetGoalDialog = prepareDialog(onSubmitClickListener = {
                        viewModel.addUserHobby(hobby, it)
                        findNavController().navigate(R.id.userHobbiesFragment)
                    })
                    setGoalDialog.show()
                }
            }
    }

    private fun configureHobbiesRecyclerViews() {
        com.yelysei.hobbyhub.ui.screens.recyclerViewConfigureView(
            com.yelysei.hobbyhub.ui.screens.Configuration(
                recyclerView = binding.recyclerViewAvailableCategorizedHobbies,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 32,
            )
        )
        com.yelysei.hobbyhub.ui.screens.recyclerViewConfigureView(
            com.yelysei.hobbyhub.ui.screens.Configuration(
                recyclerView = binding.recyclerViewSearchedCategorizedHobbies,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 32,
            )
        )
    }

    private fun setAdapter(): HobbiesAdapter {
        return HobbiesAdapter(requireContext()) {
            showAddHobbyDialog(it)
        }
    }
}