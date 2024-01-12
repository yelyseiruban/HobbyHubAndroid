package com.yelysei.hobbyhub.ui.screens.uiactions

import android.content.Context
import android.widget.Toast

class UiActionsImpl(private val context: Context?) : UiActions {
    override fun toast(text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}