package com.endava.budgetplanner.transaction.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.common.validators.contracts.Validator
import com.endava.budgetplanner.data.repo.contract.TransactionRepository
import com.endava.budgetplanner.di.annotations.TransDateValidatorQualifier
import com.endava.budgetplanner.di.annotations.TransNameValidatorQualifier
import com.endava.budgetplanner.di.annotations.TransactionAmountValidatorQualifier
import java.util.*
import javax.inject.Inject

class TransactionFactory @Inject constructor(
    private val tokenPreferences: TokenPreferences,
    private val transactionRepository: TransactionRepository,
    @TransNameValidatorQualifier
    private val transNameValidator: Validator<CharSequence, Int, Int>,
    @TransDateValidatorQualifier
    private val transDateValidator: Validator<Date, Date, Date>,
    @TransactionAmountValidatorQualifier
    private val transactionAmountValidator: Validator<Double, Double, Double>,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionViewModel(
            tokenPreferences, transNameValidator, transDateValidator,
            transactionAmountValidator, transactionRepository
        ) as T
    }
}