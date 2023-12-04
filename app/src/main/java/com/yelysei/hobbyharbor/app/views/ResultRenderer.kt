package com.yelysei.hobbyharbor.app.views

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import com.yelysei.foundation.model.Result
import com.yelysei.foundation.views.BaseFragment
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.PartResultBinding

fun <T> BaseFragment.renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
    val binding = PartResultBinding.bind(root)
    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onError = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onSuccess = {successData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        })
}

fun BaseFragment.onTryAgain(root: ViewGroup, onTryAgainPressed: () -> Unit) {
    root.findViewById<Button>(R.id.buttonTryAgain).setOnClickListener { onTryAgainPressed() }
}