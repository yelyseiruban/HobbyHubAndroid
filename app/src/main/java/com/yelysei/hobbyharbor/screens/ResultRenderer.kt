package com.yelysei.hobbyharbor.screens

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.PartResultBinding
import com.yelysei.hobbyharbor.model.results.ErrorResult
import com.yelysei.hobbyharbor.model.results.PendingResult
import com.yelysei.hobbyharbor.model.results.Result
import com.yelysei.hobbyharbor.model.results.SuccessResult

fun <T> renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
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
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        })
}

fun <T> renderResult(
    root: ViewGroup, result: Result<T>,
    onPending: () -> Unit,
    onError: (Exception) -> Unit,
    onSuccess: (T) -> Unit
) {
    root.children.forEach { it.visibility = View.GONE }
    when (result) {
        is SuccessResult -> onSuccess(result.data)
        is ErrorResult -> onError(result.exception)
        is PendingResult -> onPending()
    }
}

fun onTryAgain(root: ViewGroup, onTryAgainPressed: () -> Unit) {
    root.findViewById<Button>(R.id.buttonTryAgain).setOnClickListener { onTryAgainPressed() }
}

