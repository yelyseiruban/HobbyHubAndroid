package com.yelysei.hobbyharbor.ui.screens.main.userhobbies

import UserHobbiesAdapter
import UserHobbyActionListener
import android.content.DialogInterface.OnClickListener
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbiesBinding
import com.yelysei.hobbyharbor.model.NoUserHobbiesFoundException
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.ui.dialogs.ConfirmRemoveUserHobbiesDialog
import com.yelysei.hobbyharbor.ui.fab.appear
import com.yelysei.hobbyharbor.ui.fab.reAppear
import com.yelysei.hobbyharbor.ui.fab.setMovableBehavior
import com.yelysei.hobbyharbor.ui.screens.main.BaseFragment
import com.yelysei.hobbyharbor.utils.resources.AttributeUtils
import com.yelysei.hobbyharbor.utils.viewModelCreator


class UserHobbiesFragment : BaseFragment() {

    private val viewModel by viewModelCreator { UserHobbiesViewModel(Repositories.userHobbiesRepository) }

    private lateinit var binding: FragmentUserHobbiesBinding
    private lateinit var adapter: UserHobbiesAdapter
    private lateinit var attributeUtils: AttributeUtils
    private lateinit var addButtonIconDrawable: Drawable
    private lateinit var removeButtonIconDrawable: Drawable
    private lateinit var fabColorStateList: ColorStateList

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (adapter.isToggleEnabled) {
                adapter.unselectUserHobbies()
            } else {
                requireActivity().finish()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            viewModel.load()
        } catch (e: NoUserHobbiesFoundException) {
            uiActions.toast(stringResources.getString(R.string.no_hobbies_added_message))
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding = FragmentUserHobbiesBinding.inflate(layoutInflater, container, false)
        attributeUtils = AttributeUtils(binding.root, R.styleable.FabView)

        adapter = UserHobbiesAdapter(requireContext(),
            object : UserHobbyActionListener {
                override fun onUserHobbyDetails(userHobby: UserHobby) {
                    openHobbyDetails(userHobby.id, userHobby.hobby.hobbyName)
                }

                override fun onToggleListener(userHobbies: List<UserHobby>) {
                    binding.fab.reAppear(removeButtonIconDrawable)
                    fabActionRemoveSelectedUserHobbies(userHobbies)
                }

                override fun onChangeSelectedHobbiesListener(userHobbies: List<UserHobby>) {
                    fabActionRemoveSelectedUserHobbies(userHobbies)
                }

                override fun onUnToggleListener() {
                    reAppearFABActionAddUserHobby()
                }
            })

        binding.recyclerViewUserHobbies.adapter = adapter
        viewModel.userHobbies.observe(viewLifecycleOwner) { result ->
            com.yelysei.hobbyharbor.ui.screens.renderSimpleResult(binding.root, result) {
                adapter.proceedUserHobbies(it)
            }
        }

        com.yelysei.hobbyharbor.ui.screens.recyclerViewConfigureView(
            com.yelysei.hobbyharbor.ui.screens.Configuration(
                recyclerView = binding.recyclerViewUserHobbies,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 32,
            )
        )

        binding.fab.setMovableBehavior()
        removeButtonIconDrawable =
            attributeUtils.getDrawableFromAttribute(R.styleable.FabView_removeButtonIcon)!!
        addButtonIconDrawable =
            attributeUtils.getDrawableFromAttribute(R.styleable.FabView_addButtonIcon)!!
        fabColorStateList =
            ColorStateList.valueOf(attributeUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        attributeUtils.onClear()
        firstAppearFABActionAddUserHobby()

        return binding.root
    }

    private fun fabActionRemoveSelectedUserHobbies(userHobbies: List<UserHobby>) {
        val onPositiveButtonClickListener = OnClickListener { _, _ ->
            viewModel.removeUserHobbies(userHobbies)
            uiActions.toast(stringResources.getString(R.string.deleted_user_hobbies_toast, userHobbies.size.toString()))
            adapter.unselectUserHobbies()
        }
        val onNegativeButtonClickListener = OnClickListener { _, _ ->
            adapter.unselectUserHobbies()
        }
        binding.fab.setOnClickListener {
            val removeUserHobbyDialog = ConfirmRemoveUserHobbiesDialog(
                requireContext(),
                onPositiveButtonClickListener,
                onNegativeButtonClickListener
            )
            removeUserHobbyDialog.show()
        }
    }

    private fun reAppearFABActionAddUserHobby() {
        binding.fab.reAppear(addButtonIconDrawable)
        binding.fab.setOnClickListener {
            openCategorizedHobbies()
        }
    }

    private fun firstAppearFABActionAddUserHobby() {
        binding.fab.appear(fabColorStateList)
        binding.fab.setOnClickListener {
            openCategorizedHobbies()
        }
    }

    private fun openCategorizedHobbies() {
        findNavController().navigate(UserHobbiesFragmentDirections.actionUserHobbiesFragmentToCategorizedHobbiesFragment())
    }

    private fun openHobbyDetails(uhId: Int, hobbyName: String) {
        val direction =
            UserHobbiesFragmentDirections.actionUserHobbiesFragmentToUserHobbyDetailsFragment(
                uhId,
                hobbyName
            )
        findNavController().navigate(direction)
    }

    override fun onDestroyView() {
        callback.remove()
        super.onDestroyView()
    }
}