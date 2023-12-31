package com.yelysei.hobbyharbor.screens.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.SetGoalDialogBinding
import com.yelysei.hobbyharbor.screens.dialogs.SetGoalDialog.ProgressState
import com.yelysei.hobbyharbor.screens.uiactions.UiActions
import com.yelysei.hobbyharbor.screens.uiactions.UiActionsImpl
import com.yelysei.hobbyharbor.utils.KeyboardUtils


typealias OnSetGoalSubmitClickListener = (goal: Int) -> Unit

class SetGoalDialog private constructor(
    private val context: Context,
    private val uiActions: UiActions,
    private val onSubmitClickListener: OnSetGoalSubmitClickListener,
    private val progressState: ProgressState? = null,
) : Dialog {

    data class Builder(
        var context: Context? = null,
        var uiActions: UiActions? = null,
        var onSubmitClickListener: OnSetGoalSubmitClickListener? = null,
        var previousProgressState: ProgressState? = null,
    ) {
        fun context(context: Context) = apply { this.context = context }

        fun uiActions(uiActions: UiActions) = apply { this.uiActions = uiActions }

        fun onSubmitClickListener(onSubmitClickListener: OnSetGoalSubmitClickListener) = apply { this.onSubmitClickListener = onSubmitClickListener }

        fun previousProgressState(previousProgressState: ProgressState) = apply { this.previousProgressState = previousProgressState }

        // Check if required properties are set before building
        private fun canBuild(): Boolean {
            return context != null && uiActions != null && onSubmitClickListener != null
        }

        fun build(): SetGoalDialog {
            if (!canBuild()) {
                throw IllegalStateException("Cannot build SetGoalDialog. Required properties are not set.")
            }
            return SetGoalDialog(context!!, uiActions!!, onSubmitClickListener!!, previousProgressState)
        }
    }

    data class ProgressState(
        val previousGoal: Int,
        val reachedProgress: Int
    )

    private val binding: SetGoalDialogBinding by lazy {
        SetGoalDialogBinding.inflate(LayoutInflater.from(context))
    }

    override fun show() {
        val editGoal = binding.goal
        KeyboardUtils.showKeyboard(context, editGoal, 300)
        var editValueString = ""
        var reachedProgress = 0
        if (progressState != null) {
            editValueString = progressState.previousGoal.toString()
            reachedProgress = progressState.reachedProgress
        }
        editGoal.setText(editValueString)
        //Move cursor to the end
        editGoal.setSelection(editGoal.length())

        val editGoalLayout = binding.goalLayout

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.please_set_up_your_goal_for_the_hobby)
            .setView(binding.root)
            .setPositiveButton("Submit", null)
            .show()

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener {
            try {
                val goal: Int = editGoal.text.toString().toInt()
                if (goal >= reachedProgress) {
                    onSubmitClickListener(goal)
                        dialog.dismiss()
                } else {
                    editGoalLayout.error = "Specified goal should be greater than reached progress"
                }
            } catch (e: java.lang.NumberFormatException) {
                editGoalLayout.error = "Specified goal is empty"
            }
        }
    }
}

fun Fragment.prepareDialog(
    onSubmitClickListener: OnSetGoalSubmitClickListener,
    progressState: ProgressState? = null
): SetGoalDialog {
    return SetGoalDialog.Builder(
        requireContext(),
        UiActionsImpl(requireContext()),
        onSubmitClickListener,
        progressState
    ).build()
}
