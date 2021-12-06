package com.endava.budgetplanner.transaction.ui.vm.state

import androidx.annotation.StringRes
import com.endava.budgetplanner.data.models.DateFormatter

sealed class TransactionEvent {
    data class ErrorId(@StringRes val messageId: Int) : TransactionEvent()
    data class Error(val message: String) : TransactionEvent()
    data class Success(val amount: Double, @StringRes val template: Int) : TransactionEvent()
    data class DateSelected(val date: DateFormatter) : TransactionEvent()
    object ConnectionError : TransactionEvent()
    object NavigateBack : TransactionEvent()
    object ClearFields : TransactionEvent()
}
