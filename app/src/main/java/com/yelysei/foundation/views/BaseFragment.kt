package com.yelysei.foundation.views

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.fragment.app.Fragment
import com.yelysei.foundation.model.ErrorResult
import com.yelysei.foundation.model.PendingResult
import com.yelysei.foundation.model.Result
import com.yelysei.foundation.model.SuccessResult

abstract class BaseFragment : Fragment(){
    abstract val viewModel: BaseViewModel

    fun <T> renderResult(root: ViewGroup, result: Result<T>,
                         onPending: () -> Unit,
                         onError: (Exception) -> Unit,
                         onSuccess: (T) -> Unit) {
        root.children.forEach { it.visibility = View.GONE }
        when (result) {
            is SuccessResult -> onSuccess(result.data)
            is ErrorResult -> onError(result.exception)
            is PendingResult -> onPending()
        }
    }
}