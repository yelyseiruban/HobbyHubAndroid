package com.yelysei.hobbyharbor.screens.dialogs

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import com.yelysei.hobbyharbor.R
import com.yelysei.hobbyharbor.databinding.AddExperienceDialogBinding
import java.util.Calendar

typealias SubmitClickListener = (from: Date, till: Date) -> Unit

class AddExperienceDialog(
    private val context: Context,
    private val onSubmitClick: SubmitClickListener

) : TimePickerDialog.OnTimeSetListener {
    private val calendar = Calendar.getInstance()
    private var settingValue: SettingValue? = null
    private var from = Date(0, 0, 0, 0, 0)
    private var till = Date(0, 0, 0, 0, 0)
    private var setState = SetState()


    private val binding: AddExperienceDialogBinding by lazy {
        AddExperienceDialogBinding.inflate(LayoutInflater.from(context))
    }

    private lateinit var datePicker: DatePicker
    private lateinit var submitButton: Button
    private lateinit var tvFrom: TextView
    private lateinit var tvTill: TextView


    fun show() {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(true)
        dialog.setContentView(binding.root)

        datePicker = binding.datePicker
        submitButton = binding.submitButton
        tvFrom = binding.tvFrom
        tvTill = binding.tvTill

        val timePickerDialog = TimePickerDialog(
            context,
            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )

        datePicker.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            if (!setState.isFromSet) {
                tvFrom.text = context.getString(R.string.from_date, dayOfMonth, monthOfYear, year)
                from.setDate(dayOfMonth, monthOfYear, year)
                settingValue = SettingValue.FROM
                timePickerDialog.show()
            } else if (!setState.isTillSet) {
                tvTill.text = context.getString(R.string.till_date, dayOfMonth, monthOfYear, year)
                till.setDate(dayOfMonth, monthOfYear, year)
                settingValue = SettingValue.TILL
                timePickerDialog.show()
            }
        }

        tvFrom.setOnClickListener {
            setState.isFromSet = false
            renderSubmitButton()
        }
        tvTill.setOnClickListener {
            setState.isTillSet = false
            renderSubmitButton()
        }

        submitButton.setOnClickListener {
            onSubmitClick(from, till)
            dialog.dismiss()
        }
        dialog.show()
    }
    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        when (settingValue ?: throw Exception("Unspecified value is setting")) {
            SettingValue.FROM -> {
                from.setTime(hourOfDay, minute)
                tvFrom.text = context.getString(R.string.from_date_time,
                    from.dayOfMonth, from.monthOfYear, from.year,
                    from.hour, from.minute
                )
                setState.isFromSet = true
                renderSubmitButton()
            }
            SettingValue.TILL -> {
                till.setTime(hourOfDay, minute)
                tvTill.text = context.getString(R.string.till_date_time,
                    till.dayOfMonth, till.monthOfYear, till.year,
                    till.hour, till.minute
                )
                setState.isTillSet = true
                renderSubmitButton()
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

data class Date(
    var dayOfMonth: Int,
    var monthOfYear: Int,
    var year: Int,
    var hour: Int,
    var minute: Int,
) {
    fun setDate(dayOfMonth: Int, monthOfYear: Int, year: Int) {
        this.dayOfMonth = dayOfMonth
        this.monthOfYear = monthOfYear
        this.year = year
    }

    fun setTime(hour: Int, minute: Int) {
        this.hour = hour
        this.minute = minute
    }
}

data class SetState(
    var isFromSet: Boolean = false,
    var isTillSet: Boolean = false

) {
    fun areValuesSet(): Boolean {
        return (isFromSet && isTillSet)
    }
}

