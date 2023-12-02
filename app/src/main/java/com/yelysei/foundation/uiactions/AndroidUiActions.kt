package com.yelysei.foundation.uiactions

import android.content.Context
import android.widget.Toast

class AndroidUiActions(
    private val appContext: Context
): UiActions {
    override fun toast(messageRes: Int) {
        Toast.makeText(appContext, messageRes, Toast.LENGTH_SHORT).show()
    }

    override fun getString(messageRes: Int): String {
        return appContext.getString(messageRes)
    }
}