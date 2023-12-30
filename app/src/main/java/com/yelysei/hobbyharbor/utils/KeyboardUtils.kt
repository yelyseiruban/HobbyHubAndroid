package com.yelysei.hobbyharbor.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtils {
    fun showKeyboard(context: Context, view: View, delayTime: Long? = null) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        view.requestFocus() // Ensure the view has focus
        view.postDelayed({
                inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
            }, delayTime ?: 200L
        )
    }
}