package com.yelysei.hobbyharbor.ui.screens.main.userhobbies

import android.content.DialogInterface.OnClickListener
import android.content.res.ColorStateList
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.SharedPreferences
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbiesBinding
import com.yelysei.hobbyharbor.model.NoUserHobbiesFoundException
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.ui.dialogs.ConfirmRemoveItemsDialog
import com.yelysei.hobbyharbor.ui.fab.setMovableBehavior
import com.yelysei.hobbyharbor.ui.screens.main.BaseFragment
import com.yelysei.hobbyharbor.utils.notifications.UserHobbyReminder
import com.yelysei.hobbyharbor.utils.resources.AttributeUtils
import com.yelysei.hobbyharbor.utils.viewModelCreator

class UserHobbiesFragment : BaseFragment() {

    private val viewModel by viewModelCreator {
        UserHobbiesViewModel(
            Repositories.userHobbiesRepository,
            SharedPreferences.sharedStorage
        )
    }

    private lateinit var binding: FragmentUserHobbiesBinding
    private lateinit var adapter: UserHobbiesAdapter
    private lateinit var attributeUtils: AttributeUtils
    private lateinit var addButtonIconDrawable: Drawable
    private lateinit var removeButtonIconDrawable: Drawable
    private lateinit var currentFabIconDrawable: Drawable

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
                    currentFabIconDrawable = removeButtonIconDrawable
                    fabReAppear()
                    binding.fab.setOnClickListener {
                        removeSelectedUserHobbies(userHobbies)
                    }
                }

                override fun onChangeSelectedHobbiesListener(userHobbies: List<UserHobby>) {
                    binding.fab.setOnClickListener {
                        removeSelectedUserHobbies(userHobbies)
                    }
                }

                override fun onUnToggleListener() {
                    currentFabIconDrawable = addButtonIconDrawable
                    fabReAppear()
                    binding.fab.setOnClickListener {
                        openCategorizedHobbies()
                    }
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
        currentFabIconDrawable = addButtonIconDrawable
        fabColorStateList =
            ColorStateList.valueOf(attributeUtils.getColorFromAttribute(R.styleable.FabView_fabColor))
        attributeUtils.onClear()
        binding.fab.supportImageTintList = fabColorStateList

        binding.fab.setOnClickListener {
            openCategorizedHobbies()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        fabReAppear()
    }

    private fun fabReAppear() {
        binding.fab.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.fab.setImageDrawable(currentFabIconDrawable)
            binding.fab.show()
        }, 300)
    }

    private fun removeSelectedUserHobbies(userHobbies: List<UserHobby>) {
        val onPositiveButtonClickListener = OnClickListener { _, _ ->
            viewModel.removeUserHobbies(userHobbies)
            userHobbies.forEach {
                UserHobbyReminder.removeNotification(requireContext(), it.hobby.hobbyName, it.hobby.id!!)
            }
            uiActions.toast(
                stringResources.getString(
                    R.string.deleted_items_toast,
                    userHobbies.size.toString()
                )
            )
            adapter.unselectUserHobbies()
        }
        val onNegativeButtonClickListener = OnClickListener { _, _ ->
            adapter.unselectUserHobbies()
        }
        val removeUserHobbyDialog = ConfirmRemoveItemsDialog(
            requireContext(),
            stringResources.getString(R.string.remove_user_hobbies_title),
            stringResources.getString(R.string.remove_user_hobby_message),
            onPositiveButtonClickListener,
            onNegativeButtonClickListener
        )
        removeUserHobbyDialog.show()
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
        findNavController().navigate(
            direction
        )
    }

    override fun onDestroyView() {
        callback.remove()
        super.onDestroyView()
    }
}