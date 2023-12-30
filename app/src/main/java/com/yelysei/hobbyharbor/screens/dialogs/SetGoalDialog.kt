package com.yelysei.hobbyharbor.screens.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.SetGoalDialogBinding
import com.yelysei.hobbyharbor.screens.uiactions.UiActions
import com.yelysei.hobbyharbor.screens.uiactions.UiActionsImpl
import com.yelysei.hobbyharbor.utils.KeyboardUtils


typealias OnSetGoalSubmitClickListener = (goal: Int) -> Unit

class SetGoalDialog(
    private val context: Context,
    private val previousGoal: Int?,
    private val uiActions: UiActions,
    private val onSubmitClickListener: OnSetGoalSubmitClickListener
) : Dialog {
    private val binding: SetGoalDialogBinding by lazy {
        SetGoalDialogBinding.inflate(LayoutInflater.from(context))
    }

    override fun show() {
        val editGoal = binding.goal
        editGoal.requestFocus()
        KeyboardUtils.showKeyboard(context, editGoal, 300)
        editGoal.setText(previousGoal?.toString() ?: "")
        MaterialAlertDialogBuilder(context)
            .setTitle(R.string.please_set_up_your_goal_for_the_hobby)
            .setView(binding.root)
            .setPositiveButton("Submit") { dialog, _ ->
                try {
                    val goal: Int = editGoal.text.toString().toInt()
                    onSubmitClickListener(goal)
                } catch (e: java.lang.NumberFormatException) {
                    uiActions.toast("Specified goal is not correct")
                }
                dialog.dismiss()
            }
            .show()
    }
}

fun Fragment.prepareDialog(
    previousGoal: Int? = null,
    onSubmitClickListener: OnSetGoalSubmitClickListener
): SetGoalDialog {
    return SetGoalDialog(
        requireContext(),
        previousGoal,
        UiActionsImpl(requireContext()),
        onSubmitClickListener
    )
}
