package com.endava.budgetplanner.models

import com.endava.budgetplanner.common.utils.TransactionType
import com.endava.budgetplanner.data.models.Category

data class CategoriesFilterCase(
    val categories: List<Category>,
    val type:TransactionType,
    val expected: List<Category>
)