package com.yelysei.foundation.navigator

import com.yelysei.foundation.views.BaseScreen

interface Navigator {
    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)
}