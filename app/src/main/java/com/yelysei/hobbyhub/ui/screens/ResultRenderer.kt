package com.yelysei.hobbyhub.ui.screens

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.core.view.children
import com.yelysei.hobbyhub.R
import com.yelysei.hobbyhub.databinding.PartResultBinding
import com.yelysei.hobbyhub.model.results.ErrorResult
import com.yelysei.hobbyhub.model.results.PendingResult
import com.yelysei.hobbyhub.model.results.Result
import com.yelysei.hobbyhub.model.results.SuccessResult

fun <T> renderSimpleResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
    val binding = PartResultBinding.bind(root)
    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer }
                .forEach { it.visibility = View.VISIBLE }
            onSuccess(successData)
        })
}

fun <T> renderExperienceDetailsResult(root: ViewGroup, result: Result<T>, onSuccess: (T) -> Unit) {
    val binding = PartResultBinding.bind(root)
    renderResult(
        root = root,
        result = result,
        onPending = {
            binding.progressBar.visibility = View.VISIBLE
        },
        onError = {
            binding.errorContainer.visibility = View.VISIBLE
        },
        onSuccess = { successData ->
            root.children
                .filter { it.id != R.id.progressBar && it.id != R.id.errorContainer && it.id != R.id.addPinContainer }
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

