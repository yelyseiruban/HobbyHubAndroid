package com.yelysei.hobbyharbor.app.views.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.yelysei.hobbyharbor.databinding.FragmentCategoriesBinding

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        binding.buttonBack.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

}