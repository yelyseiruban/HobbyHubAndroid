package com.yelysei.hobbyharbor.ui.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.slider.Slider
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.SetNotificationDialogBinding

typealias OnSubmitSetNotificationClickListener = (timeInMilliseconds: Long) -> Unit
typealias OnRemoveNotificationClickListener = () -> Unit

class SetNotificationDialog(
    private val context: Context,
    private val isNotificationSet: Boolean,
    private val previousInternalTimeInMilliseconds: Long?,
    private val onPositiveButtonClickListener: OnSubmitSetNotificationClickListener,
    private val onNegativeButtonClickListener: OnRemoveNotificationClickListener
) : Dialog {

    private val binding by lazy {
        SetNotificationDialogBinding.inflate(LayoutInflater.from(context))
    }

    private lateinit var slider: Slider

    override fun show() {
        slider = binding.slider
        slider.setLabelFormatter { value: Float ->
            sliderValueToStringLabel[value]!!
        }

        val dialog = MaterialAlertDialogBuilder(context)
            .setTitle(R.string.set_notification_title)
            .setMessage(R.string.set_notification_message)
            .setView(binding.root)
            .setPositiveButton("Submit", null)
            .setNegativeButton("Remove", null)
            .show()

        if (previousInternalTimeInMilliseconds != null) {
            slider.value = timeInMillisecondsToSliderValue[previousInternalTimeInMilliseconds] ?: 0.0f
        }

        val positiveButton = dialog.getButton(AlertDialog.BUTTON_POSITIVE)
        positiveButton.setOnClickListener {
            onPositiveButtonClickListener(getTimeInMillisecondsFromSlider())
            dialog.dismiss()
        }
        val negativeButton = dialog.getButton(AlertDialog.BUTTON_NEGATIVE)
        negativeButton.setOnClickListener {
            onNegativeButtonClickListener()
            dialog.dismiss()
        }
        negativeButton.isEnabled = isNotificationSet
    }

    private fun getTimeInMillisecondsFromSlider(): Long =
        sliderValueToTimeInMilliseconds[slider.value]!!

    companion object {
        const val dayInMilliseconds: Long = 1000 * 60 * 60 * 24

        val sliderValueToTimeInMilliseconds = mapOf(
            0.0f to (dayInMilliseconds * 3),
            20.0f to (dayInMilliseconds * 7),
            40.0f to (dayInMilliseconds * 14),
            60.0f to (dayInMilliseconds * 31),
            80.0f to (dayInMilliseconds * 93),
            100.0f to (dayInMilliseconds * 186)
        )

        val timeInMillisecondsToSliderValue = mapOf(
            (dayInMilliseconds * 3) to 0.0f,
            (dayInMilliseconds * 7) to 20.0f,
            (dayInMilliseconds * 14) to 40.0f,
            (dayInMilliseconds * 31) to 60.0f,
            (dayInMilliseconds * 93) to 80.0f,
            (dayInMilliseconds * 186) to 100.0f
        )

        val sliderValueToStringLabel = mapOf(
            0.0f to "3 Days",
            20.0f to "1 Week",
            40.0f to "2 Weeks",
            60.0f to "1 Month",
            80.0f to "3 Months",
            100.0f to "6 Months"
        )
    }
}