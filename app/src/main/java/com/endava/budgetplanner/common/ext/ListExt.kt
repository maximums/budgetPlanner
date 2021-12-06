package com.endava.budgetplanner.common.ext

import com.endava.budgetplanner.common.utils.CategoryProperties
import com.endava.budgetplanner.common.utils.TransactionType
import com.endava.budgetplanner.data.models.Category

private const val MIN_NUMBER_TRANSACTIONS = 0

fun List<Category>.filterByType(transactionType: TransactionType): List<Category> {
    return filter { it.type == transactionType }.sortedWith(compareBy(
        { it.code == CategoryProperties.IN_OTHER || it.code == CategoryProperties.EX_OTHER },
        { it.name }
    ))
}

fun List<Category>.filterByNoTransactions(): List<Category> {
    return filter { it.numberOfTransactions > MIN_NUMBER_TRANSACTIONS }
}