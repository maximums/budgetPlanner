package com.endava.budgetplanner.common.base

import com.endava.budgetplanner.common.utils.TransactionType

interface RoundFormatter {

    fun format(num: Double, transactionType: TransactionType): String?
}