package com.endava.budgetplanner.transaction.ui.vm.state

sealed class TransactionValidateEvent {
    object ValidateNameSuccess : TransactionValidateEvent()
    object ValidateAmountSuccess : TransactionValidateEvent()
    object ValidateCategorySuccess : TransactionValidateEvent()
    object ValidateDateSuccess : TransactionValidateEvent()
    data class Error(val message: String) : TransactionValidateEvent()
}