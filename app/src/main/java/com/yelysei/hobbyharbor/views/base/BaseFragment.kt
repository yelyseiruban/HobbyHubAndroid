package com.yelysei.hobbyharbor.views.base

import androidx.fragment.app.Fragment
import com.yelysei.hobbyharbor.main.MainActivity

abstract class BaseFragment : Fragment(){
    abstract val viewModel: BaseViewModel

    fun notifyScreenUpdates() {
        (requireActivity() as MainActivity).notifyScreenUpdates()
    }
}