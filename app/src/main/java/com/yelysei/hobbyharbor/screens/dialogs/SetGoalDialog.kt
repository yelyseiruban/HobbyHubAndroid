package com.yelysei.hobbyharbor.screens.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import androidx.fragment.app.Fragment
import com.yelysei.hobbyharbor.databinding.SetGoalDialogBinding
import com.yelysei.hobbyharbor.screens.uiactions.UiActions
import com.yelysei.hobbyharbor.screens.uiactions.UiActionsImpl

typealias OnSubmitClickListener = (goal: Int) -> Unit

class SetGoalDialog(
    private val context: Context,
    private val previousGoal: Int?,
    private val uiActions: UiActions
) {
    private val binding: SetGoalDialogBinding by lazy {
        SetGoalDialogBinding.inflate(LayoutInflater.from(context))
    }
    fun show(onSubmitClickListener: OnSubmitClickListener) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)
        binding.goalEditText.setText(previousGoal?.toString() ?: "")
        binding.submitButton.setOnClickListener {
            try {
                val goal = binding.goalEditText.text.toString().toInt()
                onSubmitClickListener(goal)
                dialog.dismiss()
            } catch (e: java.lang.NumberFormatException) {
                uiActions.toast("Specified goal is not correct")
            }
        }
        dialog.show()
    }
}

fun Fragment.prepareDialog(previousGoal: Int? = null): SetGoalDialog {
    val context = requireContext()
    return SetGoalDialog(context, previousGoal, UiActionsImpl(context))
}
