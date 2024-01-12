package com.yelysei.hobbyhub.utils.resources

import android.content.res.Resources
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.view.View
import com.yelysei.hobbyhub.R


class AttributeUtils(
    view: View,
    styleable: IntArray
) {
    private val typedArray: TypedArray
    private val resources: Resources
    private val theme: Resources.Theme

    init {
        typedArray = view.context.obtainStyledAttributes(R.style.Base_Theme_HobbyHub, styleable)
        resources = view.context.resources
        theme = view.context.theme
    }

    fun getDrawableFromAttribute(index: Int): Drawable? {
        return typedArray.getDrawable(index)
    }

    fun getColorFromAttribute(index: Int): Int {
        return typedArray.getColor(index, resources.getColor(R.color.black, theme))
    }

    fun onClear() {
        typedArray.recycle()
    }
}