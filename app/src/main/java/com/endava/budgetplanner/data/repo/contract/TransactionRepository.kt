package com.endava.budgetplanner.data.repo.contract

import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.models.CategoryDetails
import com.endava.budgetplanner.data.models.Transaction

interface TransactionRepository {

    suspend fun addNewTransaction(token: String, transaction: Transaction): Resource<Unit>

    suspend fun getListOfTransactionsByCategory(
        token: String,
        categoryId: Int
    ): Resource<CategoryDetails>

    suspend fun deleteTransaction(token: String, transactionId: Int): Resource<Unit>
}