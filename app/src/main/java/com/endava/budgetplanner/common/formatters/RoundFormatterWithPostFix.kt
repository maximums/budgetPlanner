package com.endava.budgetplanner.common.formatters

import com.endava.budgetplanner.common.base.RoundFormatter
import com.endava.budgetplanner.common.utils.TransactionType
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

private const val DIVIDER = 1000.0
private const val DEFAULT_POST_FIX = "k"
private const val DEFAULT_DECIMAL_FORMAT = "0.00"

class RoundFormatterWithPostFix @Inject constructor() : RoundFormatter {

    override fun format(num: Double, transactionType: TransactionType): String? {
        return if (num < DIVIDER) {
            roundToTwoDecimals(num)
        } else {
            val numToRound = num / DIVIDER
            when (transactionType) {
                TransactionType.EXPENSES -> roundToTwoDecimals(numToRound).plus(
                    DEFAULT_POST_FIX
                )
                TransactionType.INCOME -> roundToTwoDecimals(numToRound, RoundingMode.FLOOR).plus(
                    DEFAULT_POST_FIX
                )
                TransactionType.UNKNOWN -> null
            }
        }
    }

    private fun roundToTwoDecimals(num: Double, mode: RoundingMode = RoundingMode.CEILING) =
        DecimalFormat(DEFAULT_DECIMAL_FORMAT)
            .apply {
                roundingMode = mode
            }.format(num)
}