package com.endava.budgetplanner.data.models

import com.endava.budgetplanner.common.base.Item
import com.endava.budgetplanner.common.utils.CategoryProperties
import com.endava.budgetplanner.common.utils.TransactionType

data class Category(
    val id: Int,
    val name: String,
    val code: CategoryProperties,
    val type: TransactionType,
    val categoryAmount: Double,
    val numberOfTransactions: Int,
    val color: String
) : Item