package com.endava.budgetplanner.transactionlist.ui.vm.state

import androidx.annotation.StringRes

sealed class TransactionListEvent {
    data class Error(val message: String) : TransactionListEvent()
    data class ErrorId(@StringRes val messageId: Int) : TransactionListEvent()
    object ConnectionError : TransactionListEvent()
}
