package com.yelysei.foundation.sideeffects.navigator.plugin

import com.yelysei.foundation.sideeffects.SideEffectMediator
import com.yelysei.foundation.sideeffects.navigator.Navigator
import com.yelysei.foundation.views.BaseScreen


class NavigatorSideEffectMediator : SideEffectMediator<Navigator>(), Navigator {

    override fun launch(screen: BaseScreen) = target {
        it.launch(screen)
    }

    override fun goBack(result: Any?) = target {
        it.goBack(result)
    }

}