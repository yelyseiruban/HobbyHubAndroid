package com.yelysei.hobbyharbor.app.views.userhobbies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.FragmentUserHobbiesBinding


class UserHobbiesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentUserHobbiesBinding.inflate(layoutInflater, container, false)

        binding.buttonAddUserHobby.setOnClickListener {
            openCategories()
        }

        return binding.root
    }

    private fun openCategories() {
        findNavController().navigate(R.id.action_userHobbiesFragment_to_categoriesFragment)
    }

    private fun openHobbyDetails() {
        findNavController().navigate(R.id.action_userHobbiesFragment_to_userHobbyDetailsFragment, bundleOf())
    }

}