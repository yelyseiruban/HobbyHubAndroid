package com.yelysei.hobbyharbor.ui.screens

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalSpaceItemDecoration(
    private val horizontalSpaceWidth: Int
) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val itemPosition = parent.getChildAdapterPosition(view)
        val itemCount = parent.adapter?.itemCount ?: 0

        // Check if the current item is not the last one
        if (itemPosition < itemCount - 1) {
            outRect.right = horizontalSpaceWidth
        } else {
            outRect.right = 0
        }
    }
}