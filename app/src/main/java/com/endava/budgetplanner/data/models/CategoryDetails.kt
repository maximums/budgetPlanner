package com.endava.budgetplanner.data.models

import com.endava.budgetplanner.common.utils.CategoryProperties
import com.endava.budgetplanner.common.utils.TransactionType

data class CategoryDetails(
    val id: Int,
    val amount: String,
    val name: String,
    val code: CategoryProperties,
    val type: TransactionType,
    val transactions: List<TransactionResponse>
)
