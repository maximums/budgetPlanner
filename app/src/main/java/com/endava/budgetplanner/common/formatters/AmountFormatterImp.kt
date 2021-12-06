package com.endava.budgetplanner.common.formatters

import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.base.AmountFormatter
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.data.models.AmountBalance
import javax.inject.Inject

class AmountFormatterImp @Inject constructor(private val assetsManager: AssetsManager) :
    AmountFormatter {
    override fun format(amount: Double) = when {
        amount < AmountFormatterValues.ONE_THOUSAND.value -> {
            AmountBalance(
                assetsManager.getResourceString(R.string.amount_formatter_less_than_1), amount
            )
        }
        amount >= AmountFormatterValues.ONE_THOUSAND.value &&
                amount < AmountFormatterValues.FIVE_THOUSAND.value -> {
            AmountBalance(
                assetsManager.getResourceString(R.string.amount_formatter_between), amount
            )
        }
        else -> {
            AmountBalance(
                assetsManager.getResourceString(R.string.amount_formatter_less_than_5), amount
            )
        }
    }
}

enum class AmountFormatterValues(val value: Double){
    ONE_THOUSAND( 1000.0),
    FIVE_THOUSAND( 5000.0),
}
