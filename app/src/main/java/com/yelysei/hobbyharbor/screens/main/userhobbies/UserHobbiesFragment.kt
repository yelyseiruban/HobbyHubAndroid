package com.yelysei.hobbyharbor.screens.main.userhobbies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbiesBinding
import com.yelysei.hobbyharbor.model.NoUserHobbiesFoundException
import com.yelysei.hobbyharbor.model.userhobbies.entities.UserHobby
import com.yelysei.hobbyharbor.screens.Configuration
import com.yelysei.hobbyharbor.screens.recyclerViewConfigureView
import com.yelysei.hobbyharbor.screens.renderSimpleResult
import com.yelysei.hobbyharbor.utils.viewModelCreator


class UserHobbiesFragment : Fragment() {

    private val viewModel by viewModelCreator { UserHobbiesViewModel(Repositories.userHobbiesRepository) }

    private lateinit var binding: FragmentUserHobbiesBinding
    private lateinit var adapter: UserHobbiesAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        try {
            viewModel.load()
        } catch (e: NoUserHobbiesFoundException) {
            Toast.makeText(context, "You have not added hobbies yet, click on add button", Toast.LENGTH_SHORT).show()
        }

        binding = FragmentUserHobbiesBinding.inflate(layoutInflater, container, false)

        adapter = UserHobbiesAdapter(object : UserHobbyActionListener {
            override fun onUserHobbyDetails(userHobby: UserHobby) {
                openHobbyDetails(userHobby.id)
            }
        })
        binding.recyclerViewUserHobbies.adapter = adapter
        viewModel.userHobbies.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result) {
                adapter.userHobbies = it
            }
        }

        recyclerViewConfigureView(
            Configuration(
                recyclerView = binding.recyclerViewUserHobbies,
                layoutManager = LinearLayoutManager(requireContext()),
                verticalItemSpace = 64,
                constraintLayout = binding.constraintLayout,
                maxHeight = 0.75f
            )
        )

        binding.buttonAddUserHobby.setOnClickListener {
            openCategories()
        }

        return binding.root
    }


    private fun openCategories() {
        findNavController().navigate(UserHobbiesFragmentDirections.actionUserHobbiesFragmentToCategoriesFragment())
    }

    private fun openHobbyDetails(uhId: Int) {
        val direction = UserHobbiesFragmentDirections.actionUserHobbiesFragmentToUserHobbyDetailsFragment(uhId)
        findNavController().navigate(direction)
    }
}