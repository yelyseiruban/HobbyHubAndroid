package com.yelysei.hobbyhub.ui.dialogs

import android.content.Context
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yelysei.hobbyhub.R
import com.yelysei.hobbyhub.databinding.SetGoalDialogBinding
import com.yelysei.hobbyhub.ui.dialogs.SetGoalDialog.ProgressState
import com.yelysei.hobbyhub.utils.KeyboardUtils
import com.yelysei.hobbyhub.utils.resources.StringResources


typealias OnSetGoalSubmitClickListener = (goal: Int) -> Unit

class SetGoalDialog(
    private val context: Context,
    private val stringResources: StringResources,
    private val onSubmitClickListener: OnSetGoalSubmitClickListener,
    private val progressState: ProgressState? = null,
) : Dialog {

    data class ProgressState(
        val previousGoal: Int,
        val reachedProgress: Int
    )

    private val binding: SetGoalDialogBinding by lazy {
        SetGoalDialogBinding.inflate(LayoutInflater.from(context))
    }

    override fun show() {
        val editGoal = binding.goal
        KeyboardUtils.showKeyboard(context, editGoal, 500)
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
            .setTitle(R.string.set_goal_title)
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
                    editGoalLayout.error = stringResources.getString(R.string.specified_goal_less_than_reach_progress)
                }
            } catch (e: java.lang.NumberFormatException) {
                editGoalLayout.error = stringResources.getString(R.string.specified_goal_is_empty)
            }
        }
    }
}

fun Fragment.prepareDialog(
    onSubmitClickListener: OnSetGoalSubmitClickListener,
    progressState: ProgressState? = null
): SetGoalDialog {
    return SetGoalDialog(
        requireContext(),
        StringResources(requireContext()),
        onSubmitClickListener,
        progressState
    )
}
