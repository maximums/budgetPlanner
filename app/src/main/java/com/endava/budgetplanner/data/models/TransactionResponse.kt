package com.endava.budgetplanner.data.models

import com.endava.budgetplanner.common.base.Item

data class TransactionResponse(
    val id: Int,
    val name: String,
    val amount: String,
    val date: String
) : Item
