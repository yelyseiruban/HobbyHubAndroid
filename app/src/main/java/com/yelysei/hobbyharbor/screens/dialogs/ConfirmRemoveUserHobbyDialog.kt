package com.yelysei.hobbyharbor.screens.dialogs

import android.content.Context
import android.content.DialogInterface.OnClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yelysei.hobbyharbor.R
class ConfirmRemoveUserHobbyDialog(
    private val context: Context,
    private val userHobbyName: String,
    private val onPositiveClickListener: OnClickListener,
    private val onNegativeClickListener: OnClickListener
) {
    fun show() {
        MaterialAlertDialogBuilder(context)
            .setTitle(context.getString(R.string.remove_user_hobby_title, userHobbyName))
            .setMessage(R.string.remove_user_hobby_message)
            .setNegativeButton(R.string.no_button_text, onNegativeClickListener)
            .setPositiveButton(R.string.yes_button_text, onPositiveClickListener)
            .show()
    }
}