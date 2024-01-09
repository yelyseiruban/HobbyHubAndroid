package com.yelysei.hobbyharbor.ui.screens.main

import androidx.fragment.app.Fragment
import com.yelysei.hobbyharbor.ui.screens.uiactions.UiActions
import com.yelysei.hobbyharbor.ui.screens.uiactions.UiActionsImpl
import com.yelysei.hobbyharbor.utils.resources.StringResources

open class BaseFragment : Fragment() {
    open val uiActions: UiActions by lazy {
        UiActionsImpl(requireContext())
    }

    open val stringResources: StringResources by lazy {
        StringResources(requireContext())
    }
}