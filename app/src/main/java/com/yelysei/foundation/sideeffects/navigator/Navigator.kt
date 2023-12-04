package com.yelysei.foundation.sideeffects.navigator

import com.yelysei.foundation.views.BaseScreen

interface Navigator {
    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)
}