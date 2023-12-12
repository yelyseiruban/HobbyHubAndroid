package com.yelysei.hobbyharbor.screens.main.categories

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentCategoriesBinding
import com.yelysei.hobbyharbor.screens.Configuration
import com.yelysei.hobbyharbor.screens.onTryAgain
import com.yelysei.hobbyharbor.screens.recyclerViewConfigureView
import com.yelysei.hobbyharbor.screens.renderSimpleResult
import com.yelysei.hobbyharbor.utils.getScreenWidthInDp
import com.yelysei.hobbyharbor.utils.viewModelCreator

class CategoriesFragment : Fragment() {

    private lateinit var binding: FragmentCategoriesBinding

    private val viewModel by viewModelCreator { CategoriesViewModel(Repositories.hobbiesRepository) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesBinding.inflate(inflater, container, false)

        val adapter = CategoriesAdapter(object: CategoriesActionListener{
            override fun onCategoryClick(categoryName: String) {
                val direction = CategoriesFragmentDirections.actionCategoriesFragmentToCategorizedHobbiesFragment(categoryName)
                findNavController().navigate(direction)
            }
        })

        viewModel.categories.observe(viewLifecycleOwner) { result ->
            renderSimpleResult(binding.root, result, onSuccess = {
                adapter.categories = it
            })
        }

        onTryAgain(binding.root) {
            viewModel.tryAgain()
        }

        binding.buttonNavigateUp.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.recyclerViewCategories.adapter = adapter

        //width in db without paddings
        val widthInDp = getScreenWidthInDp(requireContext()) - 40
        val possibleSpanCount = (widthInDp / 150).toInt()
        val spanCount = if (150 * possibleSpanCount + 50 * (possibleSpanCount - 1) < widthInDp) possibleSpanCount else possibleSpanCount - 1

        recyclerViewConfigureView(Configuration(
            recyclerView = binding.recyclerViewCategories,
            layoutManager = GridLayoutManager(requireContext(), spanCount),
            verticalItemSpace = 64,
            constraintLayout = binding.constraintLayout,
            maxHeight = 0.75f
        ))

        return binding.root
    }

}