package com.endava.budgetplanner.common.formatters

import com.endava.budgetplanner.common.base.RoundFormatter
import com.endava.budgetplanner.common.utils.TransactionType
import java.math.RoundingMode
import java.text.DecimalFormat
import javax.inject.Inject

private const val DEFAULT_DECIMAL_FORMAT = "0.00"

class DefaultRoundFormatter @Inject constructor() : RoundFormatter {

    override fun format(num: Double, transactionType: TransactionType): String? {
        return when (transactionType) {
            TransactionType.EXPENSES -> roundToTwoDecimals(num)
            TransactionType.INCOME -> roundToTwoDecimals(
                num,
                RoundingMode.FLOOR
            )
            TransactionType.UNKNOWN -> null
        }
    }

    private fun roundToTwoDecimals(num: Double, mode: RoundingMode = RoundingMode.CEILING) =
        DecimalFormat(DEFAULT_DECIMAL_FORMAT)
            .apply {
                roundingMode = mode
            }.format(num)
}