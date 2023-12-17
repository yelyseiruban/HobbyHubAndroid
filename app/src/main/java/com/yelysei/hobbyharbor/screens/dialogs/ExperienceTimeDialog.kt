package com.yelysei.hobbyharbor.screens.dialogs

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import com.yelysei.hobbyharbor.databinding.ExperienceTimeDialogBinding
import com.yelysei.hobbyharbor.utils.getFormattedDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar

typealias SubmitClickListener = (fromDateTime: Long, tillDateTime: Long) -> Unit

class ExperienceTimeDialog(
    private val context: Context,
    private val onSubmitClick: SubmitClickListener,
    private val fragmentManager: FragmentManager

)  {
    private var settingValue: SettingValue = SettingValue.FROM
    private var setState = SetState()

    var fromDate: Long = 0
        set(value) {
            field = value
            fromDateTime = value + fromTime
        }

    var fromTime: Long = 0
        set(value) {
            field = value
            fromDateTime = value + fromDate
        }

    var tillDate: Long = 0
        set(value) {
            field = value
            tillDateTime = value + tillTime
        }

    var tillTime: Long = 0
        set(value) {
            field = value
            tillDateTime = value + tillDate
        }

    var fromDateTime: Long = 0
        set(value) {
            field = value
            setDate(SettingValue.FROM)
        }
    var tillDateTime: Long = 0
        set(value) {
            field = value
            setDate(SettingValue.TILL)
        }
    fun fulfillFromDateTime(fromDateTime: Long) {
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(fromDateTime), ZoneId.systemDefault())
        fromDate = localDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        fromTime = localDateTime.toLocalTime().toSecondOfDay() * 1000L
    }

    fun fulfillTillDateTime(tillDateTime: Long) {
        val localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(tillDateTime), ZoneId.systemDefault())
        tillDate = localDateTime.toLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli()
        tillTime = localDateTime.toLocalTime().toSecondOfDay() * 1000L
    }

    private val binding: ExperienceTimeDialogBinding by lazy {
        ExperienceTimeDialogBinding.inflate(LayoutInflater.from(context))
    }

    private lateinit var submitButton: Button
    private lateinit var tvFrom: Button
    private lateinit var tvTill: Button


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
            when (settingValue) {
                SettingValue.FROM -> {
                    fromDate = it
                }
                SettingValue.TILL -> {
                    tillDate = it
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
                    fromTime = selectedTimeInMilliseconds.toLong()
                }
                SettingValue.TILL -> {
                    tillTime = selectedTimeInMilliseconds.toLong()
                    renderSubmitButton()
                }
            }
        }

        submitButton.setOnClickListener {
            if (tillDateTime > fromDateTime) {
                onSubmitClick(fromDateTime, tillDateTime)
                dialog.dismiss()
            } else {
                Toast.makeText(context, "Till Date Time is before From", Toast.LENGTH_SHORT).show()
            }
        }

    }

    fun setDate(settingValue: SettingValue) {
        when (settingValue) {
            SettingValue.FROM -> {
                val selectedDate = getFormattedDate(fromDateTime, "dd.MM.yyyy HH:mm")
                binding.fromValue.text = selectedDate
                setState.isFromSet = true
            }
            SettingValue.TILL -> {
                val selectedDate = getFormattedDate(tillDateTime, "dd.MM.yyyy HH:mm")
                binding.tillValue.text = selectedDate
                setState.isTillSet = true
            }
        }
        renderSubmitButton()
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

