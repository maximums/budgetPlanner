package com.endava.budgetplanner.transaction.ui.vm.state

sealed class TransactionState {
    object Empty : TransactionState()
    object Loading : TransactionState()
}
