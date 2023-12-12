package com.yelysei.hobbyharbor.screens

import android.util.TypedValue
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

fun Fragment.recyclerViewConfigureView(configuration: Configuration) {
    val recyclerViewId = configuration.recyclerView.id
    //recycler view look configuration
    configuration.recyclerView.layoutManager = configuration.layoutManager
    //recycler view space between items
    configuration.recyclerView.addItemDecoration(
        VerticalSpaceItemDecoration(
            configuration.verticalItemSpace
        )
    )

    if (configuration.maxHeight != null && configuration.constraintLayout != null){
        val constraintSet = ConstraintSet()
        constraintSet.clone(configuration.constraintLayout)

        val tv = TypedValue()
        if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
            val maxHeightInPixels = (configuration.maxHeight * resources.displayMetrics.heightPixels - actionBarHeight).toInt()
            constraintSet.constrainHeight(recyclerViewId, ConstraintSet.WRAP_CONTENT)
            constraintSet.constrainMaxHeight(recyclerViewId, maxHeightInPixels)

            constraintSet.applyTo(configuration.constraintLayout)
        }
    }
}

data class Configuration(
    val recyclerView: RecyclerView,
    val layoutManager: LinearLayoutManager,
    val verticalItemSpace: Int,
    val constraintLayout: ConstraintLayout? = null,
    val maxHeight: Float? = null,
)