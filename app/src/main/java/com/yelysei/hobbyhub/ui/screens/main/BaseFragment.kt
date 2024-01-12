package com.yelysei.hobbyhub.ui.screens.main

import androidx.fragment.app.Fragment
import com.yelysei.hobbyhub.ui.screens.uiactions.UiActions
import com.yelysei.hobbyhub.ui.screens.uiactions.UiActionsImpl
import com.yelysei.hobbyhub.utils.resources.StringResources

open class BaseFragment : Fragment() {
    open val uiActions: UiActions by lazy {
        UiActionsImpl(requireContext())
    }

    open val stringResources: StringResources by lazy {
        StringResources(requireContext())
    }
}