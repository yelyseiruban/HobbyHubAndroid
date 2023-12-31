package com.yelysei.hobbyharbor.utils

import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText

object KeyboardUtils {
    fun showKeyboard(context: Context, editText: EditText, delayTime: Long? = null) {
        val inputMethodManager =
            context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        editText.requestFocus() // Ensure the view has focus
        editText.postDelayed({
                inputMethodManager.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT)
            }, delayTime ?: 200L
        )
    }
}