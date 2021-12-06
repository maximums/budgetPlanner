package com.endava.budgetplanner.transactionlist.ui.vm.state

import com.endava.budgetplanner.data.models.TransactionResponse

sealed class TransactionListState {
    object Empty : TransactionListState()
    object Loading : TransactionListState()
    data class Success(
        val transactions: List<TransactionResponse>,
        val categoryName: String,
        val amount: String
    ) : TransactionListState()
}
