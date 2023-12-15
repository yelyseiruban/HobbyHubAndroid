package com.yelysei.hobbyharbor.screens.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.AddExperienceDialogBinding
import com.yelysei.hobbyharbor.utils.getFormattedDate
import java.util.Calendar

typealias SubmitClickListener = (from: Long, till: Long) -> Unit

class AddExperienceDialog(
    private val context: Context,
    private val onSubmitClick: SubmitClickListener,
    private val fragmentManager: FragmentManager

)  {
    private var settingValue: SettingValue = SettingValue.FROM
    private var setState = SetState()
    private var from: Long = 0
    private var till: Long = 0

    private val binding: AddExperienceDialogBinding by lazy {
        AddExperienceDialogBinding.inflate(LayoutInflater.from(context))
    }

    private lateinit var submitButton: Button
    private lateinit var tvFrom: TextView
    private lateinit var tvTill: TextView


    fun show() {
        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        dialog.show()

        val datePickerDialog =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .build()

        val timePickerDialog = MaterialTimePicker.Builder()
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(currentHour)
            .setMinute(currentMinute)
            .build()

        submitButton = binding.submitButton
        tvFrom = binding.tvFrom
        tvTill = binding.tvTill

        tvFrom.setOnClickListener {
            datePickerDialog.show(fragmentManager, "date range picker")
            settingValue = SettingValue.FROM
            renderSubmitButton()
        }

        tvTill.setOnClickListener {
            datePickerDialog.show(fragmentManager, "date range picker")
            settingValue = SettingValue.TILL
            renderSubmitButton()
        }

        datePickerDialog.addOnPositiveButtonClickListener {
            val selectedDate = getFormattedDate(it, "dd.MM.yyyy")
            when (settingValue) {
                SettingValue.FROM -> {
                    tvFrom.text = context.getString(R.string.from_date, selectedDate)
                    from = it
                }
                SettingValue.TILL -> {
                    tvTill.text = context.getString(R.string.till_date, selectedDate)
                    till = it
                }
            }
            timePickerDialog.show(fragmentManager, "time picker dialog")
        }

        timePickerDialog.addOnPositiveButtonClickListener {
            val selectedHour = timePickerDialog.hour
            val selectedMinute = timePickerDialog.minute
            val selectedTimeInMilliseconds = (selectedHour * 60 + selectedMinute) * 60000
            when (settingValue) {
                SettingValue.FROM -> {
                    from += selectedTimeInMilliseconds
                    val selectedDate = getFormattedDate(from, "dd.MM.yyyy HH:mm")
                    tvFrom.text = context.getString(R.string.from_date, selectedDate)
                    setState.isFromSet = true
                    renderSubmitButton()
                }
                SettingValue.TILL -> {
                    till += selectedTimeInMilliseconds
                    val selectedDate = getFormattedDate(till, "dd.MM.yyyy HH:mm")

                    tvTill.text = context.getString(R.string.till_date, selectedDate)
                    setState.isTillSet = true
                    renderSubmitButton()
                }
            }
        }

        submitButton.setOnClickListener {
            if (till > from) {
                onSubmitClick(from, till)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Till Date Time is before From", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun renderSubmitButton() {
        if (setState.areValuesSet()) {
            submitButton.visibility = View.VISIBLE
        } else {
            submitButton.visibility = View.GONE
        }
    }
}

enum class SettingValue {
    FROM, TILL
}


data class SetState(
    var isFromSet: Boolean = false,
    var isTillSet: Boolean = false

) {
    fun areValuesSet(): Boolean {
        return (isFromSet && isTillSet)
    }
}

