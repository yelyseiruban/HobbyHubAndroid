package com.yelysei.hobbyharbor.navigator

import androidx.annotation.StringRes
import com.yelysei.hobbyharbor.views.screens.base.BaseScreen

interface Navigator {
    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)

    fun toast(@StringRes messageRes: Int)

    fun getString(@StringRes messageRes: Int): String
}