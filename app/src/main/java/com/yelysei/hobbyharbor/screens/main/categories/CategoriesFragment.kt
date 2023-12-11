package com.yelysei.hobbyharbor.screens.main.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.Repositories
import com.yelysei.hobbyharbor.databinding.FragmentCategoriesBinding
import com.yelysei.hobbyharbor.screens.VerticalSpaceItemDecoration
import com.yelysei.hobbyharbor.screens.main.userhobbies.UserHobbiesFragment.Companion.VERTICAL_ITEM_SPACE
import com.yelysei.hobbyharbor.screens.onTryAgain
import com.yelysei.hobbyharbor.screens.renderSimpleResult
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
                val direction: CategoriesFragmentDirections()
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

        binding.buttonBack.setOnClickListener {
            viewModel.onBackPressed()
        }

        //width in db without paddings
        val widthInDp = getScreenWidthInDp(requireContext()) - 40
        Log.d("widthInPixels", widthInDp.toString())
        val possibleSpanCount = (widthInDp / 150).toInt()
        val spanCount = if (150 * possibleSpanCount + 50 * (possibleSpanCount - 1) < widthInDp) possibleSpanCount else possibleSpanCount - 1
        Log.d("spanCount", spanCount.toString())
        val layoutManager = GridLayoutManager(requireContext(), spanCount)
        binding.recyclerViewCategories.layoutManager = layoutManager
        binding.recyclerViewCategories.addItemDecoration(
            VerticalSpaceItemDecoration(
                VERTICAL_ITEM_SPACE
            )
        )

        binding.recyclerViewCategories.adapter = adapter

        //recycler view max height 75% of screen height
        val constraintSet = ConstraintSet()
        constraintSet.clone(binding.constraintLayout)
        val maxHeightInPixels = (0.65 * resources.displayMetrics.heightPixels).toInt()
        constraintSet.constrainHeight(R.id.recyclerViewUserHobbies, ConstraintSet.WRAP_CONTENT)
        constraintSet.constrainMaxHeight(R.id.recyclerViewUserHobbies, maxHeightInPixels)

        constraintSet.applyTo(binding.constraintLayout)

        return binding.root
    }

}