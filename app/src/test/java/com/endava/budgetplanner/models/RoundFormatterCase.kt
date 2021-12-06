package com.endava.budgetplanner.models

import com.endava.budgetplanner.common.utils.TransactionType

data class RoundFormatterCase(
    val num: Double,
    val transactionType: TransactionType,
    val expected: String
)
