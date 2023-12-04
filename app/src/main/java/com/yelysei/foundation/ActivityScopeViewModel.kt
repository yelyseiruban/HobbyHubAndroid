package com.yelysei.foundation

import androidx.lifecycle.ViewModel
import com.yelysei.foundation.sideeffects.SideEffectMediator
import com.yelysei.foundation.sideeffects.SideEffectMediatorsHolder

const val ARG_SCREEN = "ARG_SCREEN"

/**
 * Holder for side-effects mediators.
 * It is based on activity view-model because instances of side-effect mediators
 * should be available from fragments' view-models (usually they are passed to the view-model constructor).
 */
class ActivityScopeViewModel : ViewModel() {

    internal val sideEffectMediatorsHolder = SideEffectMediatorsHolder()

    val sideEffectMediators: List<SideEffectMediator<*>>
        get() = sideEffectMediatorsHolder.mediators
    override fun onCleared() {
        super.onCleared()
        sideEffectMediatorsHolder.clear()
    }
}
