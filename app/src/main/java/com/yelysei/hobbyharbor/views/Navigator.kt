package com.yelysei.hobbyharbor.views

import com.yelysei.hobbyharbor.views.base.BaseScreen

interface Navigator {
    fun launch(screen: BaseScreen)

    fun goBack(result: Any? = null)
}