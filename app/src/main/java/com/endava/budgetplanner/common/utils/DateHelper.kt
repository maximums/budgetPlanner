package com.endava.budgetplanner.common.utils

import com.endava.budgetplanner.data.models.DateFormatter
import java.text.SimpleDateFormat
import java.util.*

class DateHelper {

    companion object {
        private const val DATE_PATTERN = "dd/MM/yyy"

        fun getCurrentDate(pattern: String = DATE_PATTERN): DateFormatter {
            val time = Calendar.getInstance().time
            return DateFormatter(time, SimpleDateFormat(pattern).format(time))
        }

        fun formatDate(date: Date, pattern: String = DATE_PATTERN): DateFormatter {
            return DateFormatter(date, SimpleDateFormat(pattern).format(date))
        }

        fun formatDate(
            day: Int,
            month: Int,
            year: Int,
            pattern: String = DATE_PATTERN
        ): DateFormatter {
            val date = Calendar.getInstance().apply {
                set(year, month, day)
            }.time
            return DateFormatter(date, SimpleDateFormat(pattern).format(date))
        }

        fun getYearAgo(pattern: String = DATE_PATTERN): DateFormatter {
            val date = Calendar.getInstance()
            date.add(Calendar.YEAR, -1)
            return DateFormatter(date.time, SimpleDateFormat(pattern).format(date.time))
        }

        fun getTomorrowDay(pattern: String = DATE_PATTERN): DateFormatter {
            val date = Calendar.getInstance()
            date.add(Calendar.DAY_OF_MONTH, 1)
            return DateFormatter(date.time, SimpleDateFormat(pattern).format(date.time))
        }
    }
}