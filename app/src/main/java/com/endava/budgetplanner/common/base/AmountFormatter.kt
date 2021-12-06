package com.endava.budgetplanner.common.base

import com.endava.budgetplanner.data.models.AmountBalance

interface AmountFormatter {
    fun format(amount: Double): AmountBalance
}