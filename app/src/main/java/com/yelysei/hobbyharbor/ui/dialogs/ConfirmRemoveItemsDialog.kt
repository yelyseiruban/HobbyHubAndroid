package com.yelysei.hobbyharbor.ui.dialogs

import android.content.Context
import android.content.DialogInterface.OnClickListener
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.yelysei.hobbyharbor.R

class ConfirmRemoveItemsDialog(
    private val context: Context,
    private val title: String,
    private val message: String,
    private val onPositiveClickListener: OnClickListener,
    private val onNegativeClickListener: OnClickListener
) : Dialog {
    override fun show() {
        MaterialAlertDialogBuilder(context)
            .setTitle(title)
            .setMessage(message)
            .setNegativeButton(R.string.no_button_text, onNegativeClickListener)
            .setPositiveButton(R.string.yes_button_text, onPositiveClickListener)
            .show()
    }
}