package com.endava.budgetplanner.transaction.ui.dialogs

import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.endava.budgetplanner.R
import java.util.*

private const val PREVIOUS_YEAR_INDEX = -1

class CustomDatePickerDialog(
    private val callback: (year: Int, month: Int, day: Int) -> Unit
) : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        return DatePickerDialog(
            requireContext(),
            R.style.CustomDatePickerStyle,
            { _, yearSet, monthSet, dayOfMonth -> callback(yearSet, monthSet, dayOfMonth) },
            year,
            month,
            day
        ).apply {
//            datePicker.minDate = c.apply {
//                add(Calendar.YEAR, PREVIOUS_YEAR_INDEX)
//            }.time.time
        }
    }

    companion object {

        const val TAG = "date_picker_tag"

        fun newInstance(callback: (Int, Int, Int) -> Unit) = CustomDatePickerDialog(callback)
    }
}