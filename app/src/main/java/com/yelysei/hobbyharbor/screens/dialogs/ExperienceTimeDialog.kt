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

/**
 * Experience Time Dialog is used to add new user experience or change existing
 *
 * onSubmitClick is typealias describe what to do with [fromDateTime] value and [tillDateTime] value
 * when user click submit button
 *
 * @throws Exception in unreal situations
 */
class ExperienceTimeDialog(
    private val title: String,
    private val context: Context,
    private val onSubmitClick: SubmitClickListener,
    private val fragmentManager: FragmentManager

)  {
    /**
     * Describes current which value is currently setting [SettingValue.FROM] or [SettingValue.TILL]
     */
    private var settingValue: SettingValue = SettingValue.FROM

    /**
     * Describes state of set values, which values were set
     */
    private var setState = SetState()

    /**
     * Values above are used to make changes with dates or times.
     * The change of date cant affect the time value.
     *
     * For instance if there were date with time. It is possible to change only date
     * then cancel the time dialog, the previous time will stay remain
     */
    private var fromDate: Long = 0
        set(value) {
            field = value
            fromDateTime = value + fromTime
        }

    private var fromTime: Long = 0
        set(value) {
            field = value
            fromDateTime = value + fromDate
        }

    private var tillDate: Long = 0
        set(value) {
            field = value
            tillDateTime = value + tillTime
        }

    private var tillTime: Long = 0
        set(value) {
            field = value
            tillDateTime = value + tillDate
        }

    private var fromDateTime: Long = 0
        set(value) {
            field = value
            setDate(SettingValue.FROM)
        }
    private var tillDateTime: Long = 0
        set(value) {
            field = value
            setDate(SettingValue.TILL)
        }


    /**
     * [datePickerDialog] and [timePickerDialog] are used to build Material Design Picker Dialogs
     * with set current date and time in them.
     *
     * [datePickerDialog] is used in show method
     * in positiveButtonClickListener opens timePickerDialog
     */
    private var datePickerDialog: MaterialDatePicker<Long>? = null
        set(value) {
            field = value
            try {
                datePickerDialog?.show(fragmentManager, datePickerDialog.toString()) ?: throw IllegalStateException("Date picker dialog was not initialized")
                datePickerDialog?.addOnPositiveButtonClickListener {
                    val timePickerDialogBuilder = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                    when (settingValue) {
                        SettingValue.FROM -> {
                            fromDate = it
                            if (fromTime != 0L) {
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
                    renderSubmitButton()
                }
            } catch (e: IllegalStateException) {
                Toast.makeText(context, "Date picker dialog was not initialized, feel free to contact with developer team", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    private var timePickerDialog: MaterialTimePicker? = null
        set(value) {
            field = value
            try {
                timePickerDialog?.show(fragmentManager, timePickerDialog.toString()) ?: throw IllegalStateException("Time picker dialog was not initialized")
                timePickerDialog?.addOnPositiveButtonClickListener {
                    val selectedHour = timePickerDialog?.hour ?: throw IllegalStateException("Hour is null")
                    val selectedMinute = timePickerDialog?.minute ?: throw IllegalStateException("Minute is null")
                    val selectedTimeInMilliseconds = (selectedHour * 60 + selectedMinute) * 60000
                    when (settingValue) {
                        SettingValue.FROM -> {
                            fromTime = selectedTimeInMilliseconds.toLong()
                        }
                        SettingValue.TILL -> {
                            tillTime = selectedTimeInMilliseconds.toLong()
                        }
                    }
                    renderSubmitButton()
                }
            } catch (e: IllegalStateException) {
                Toast.makeText(context, "Hour and minute was not specified correctly", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
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

        binding.tvTitle.text = title
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

    private fun setDate(settingValue: SettingValue) {
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

    private fun renderSubmitButton() {
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

