package com.yelysei.hobbyharbor.app.views.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.GridLayoutManager
import com.yelysei.foundation.utils.getScreenWidthInDp
import com.yelysei.foundation.views.BaseFragment
import com.yelysei.foundation.views.BaseScreen
import com.yelysei.foundation.views.VerticalSpaceItemDecoration
import com.yelysei.foundation.views.screenViewModel
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.app.views.onTryAgain
import com.yelysei.hobbyharbor.app.views.renderSimpleResult
import com.yelysei.hobbyharbor.databinding.FragmentCategoriesBinding

class CategoriesFragment : BaseFragment() {
    override val viewModel by screenViewModel<CategoriesViewModel>()

    class Screen : BaseScreen

    private lateinit var binding: FragmentCategoriesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        val adapter = CategoriesAdapter(object: CategoriesActionListener{
            override fun onCategoryClick(categoryName: String) {
                viewModel.onCategoryClick(categoryName)
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

    companion object {
        const val VERTICAL_ITEM_SPACE = 64
    }
}