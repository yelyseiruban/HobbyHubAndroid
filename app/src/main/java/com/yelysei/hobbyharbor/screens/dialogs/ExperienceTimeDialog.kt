package com.yelysei.hobbyharbor.screens.dialogs

import android.app.Dialog
import android.content.Context
import android.util.Log
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

    var datePickerDialog: MaterialDatePicker<Long>? = null
        set(value) {
            field = value
            datePickerDialog?.show(fragmentManager, datePickerDialog.toString()) ?: throw Exception("date picker dialog was not initialized")
            datePickerDialog?.addOnPositiveButtonClickListener {
                val timePickerDialogBuilder = MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                when (settingValue) {
                    SettingValue.FROM -> {
                        fromDate = it
                        if (fromTime != 0L) {
                            Log.d("debug", fromTime.toString())
                            timePickerDialogBuilder
                                .setHour((fromTime / 3600000).toInt())
                                .setMinute(((fromTime % 3600000) / 60000).toInt())
                        } else {
                            val calendar = Calendar.getInstance()
                            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                            val currentMinute = calendar.get(Calendar.MINUTE)
                            timePickerDialogBuilder
                                .setHour(currentHour)
                                .setMinute(currentMinute)
                        }
                    }
                    SettingValue.TILL -> {
                        tillDate = it
                        if (tillTime != 0L) {
                            timePickerDialogBuilder
                                .setHour((tillTime / 3600000).toInt())
                                .setMinute(((tillTime % 3600000) / 60000).toInt())
                        } else {
                            val calendar = Calendar.getInstance()
                            val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
                            val currentMinute = calendar.get(Calendar.MINUTE)
                            timePickerDialogBuilder
                                .setHour(currentHour)
                                .setMinute(currentMinute)
                        }
                    }
                }
                timePickerDialog = timePickerDialogBuilder.build()
            }
        }
    var timePickerDialog: MaterialTimePicker? = null
        set(value) {
            field = value
            timePickerDialog?.show(fragmentManager, timePickerDialog.toString()) ?: throw Exception("time picker dialog was not initialized")
            timePickerDialog?.addOnPositiveButtonClickListener {
                val selectedHour = timePickerDialog?.hour ?: throw Exception()
                val selectedMinute = timePickerDialog?.minute ?: throw Exception()
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
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        dialog.show()

        val datePickerDialogBuilder =
            MaterialDatePicker.Builder.datePicker()
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())

        submitButton = binding.submitButton
        tvFrom = binding.tvFrom
        tvTill = binding.tvTill

        tvFrom.setOnClickListener {
            if (fromDateTime != 0L) {
                datePickerDialogBuilder.setSelection(fromDateTime)
            } else {
                datePickerDialogBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            }
            datePickerDialog = datePickerDialogBuilder.build()
            settingValue = SettingValue.FROM
            renderSubmitButton()
        }

        tvTill.setOnClickListener {
            if (tillDateTime != 0L) {
                datePickerDialogBuilder.setSelection(tillDateTime)
            } else {
                datePickerDialogBuilder.setSelection(MaterialDatePicker.todayInUtcMilliseconds())
            }
            datePickerDialog = datePickerDialogBuilder.build()
            settingValue = SettingValue.TILL
            renderSubmitButton()
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

