package com.endava.budgetplanner.transaction.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.common.utils.DateHelper
import com.endava.budgetplanner.common.utils.TransactionType
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.Validator
import com.endava.budgetplanner.data.models.Transaction
import com.endava.budgetplanner.data.repo.contract.TransactionRepository
import com.endava.budgetplanner.transaction.ui.vm.state.TransactionEvent
import com.endava.budgetplanner.transaction.ui.vm.state.TransactionState
import com.endava.budgetplanner.transaction.ui.vm.state.TransactionValidateEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import java.util.*

class TransactionViewModel(
    private val tokenPreferences: TokenPreferences,
    private val transNameValidator: Validator<CharSequence, Int, Int>,
    private val transDateValidator: Validator<Date, Date, Date>,
    private val transactionAmountValidator: Validator<Double, Double, Double>,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow<TransactionState>(TransactionState.Empty)
    val state get() = _state.asStateFlow()

    private val _channel = Channel<TransactionEvent>()
    val channel get() = _channel.receiveAsFlow()

    private val _channelValidation = Channel<TransactionValidateEvent>()
    val channelValidation get() = _channelValidation.receiveAsFlow()

    private suspend fun getToken(): String? {
        return tokenPreferences.getData { it[TokenPreferences.TOKEN_KEY] }.first()
    }

    fun addNewTransaction(transaction: Transaction) = viewModelScope.launch {
        val token = getToken()
        if (token != null) {
            _state.value = TransactionState.Loading
            val resource = transactionRepository.addNewTransaction(token, transaction)
            val event: TransactionEvent = when (resource) {
                Resource.ConnectionError -> TransactionEvent.ConnectionError
                is Resource.Error -> TransactionEvent.Error(resource.message)
                is Resource.Success -> TransactionEvent.ErrorId(R.string.unknown_error_try_again)
                Resource.SuccessEmpty -> {
                    if (TransactionType.fromType(transaction.type) == TransactionType.EXPENSES) {
                        TransactionEvent.Success(
                            transaction.amount,
                            R.string.new_expense_transaction_template
                        )
                    } else {
                        TransactionEvent.Success(
                            transaction.amount,
                            R.string.new_income_transaction_template
                        )
                    }
                }
            }
            _state.value = TransactionState.Empty
            _channel.send(event)
        } else {
            _channel.send(TransactionEvent.ErrorId(R.string.invalid_token_error))
        }
    }

    fun saveMenuItemClick(transaction: Transaction, maxAmount: Double?, transDate: Date) {
        viewModelScope.launch {
            if (handleValidationResult(transNameValidator.getValidationResult(transaction.name))) {
                _channelValidation.send(TransactionValidateEvent.ValidateNameSuccess)
                if (handleValidationResult(
                        transactionAmountValidator.getValidationResult(
                            transaction.amount,
                            max = maxAmount
                        )
                    )
                ) {
                    _channelValidation.send(TransactionValidateEvent.ValidateAmountSuccess)
                    if (handleValidationResult(transDateValidator.getValidationResult(transDate))) {
                        _channelValidation.send(TransactionValidateEvent.ValidateDateSuccess)
                        //validate category
                        addNewTransaction(transaction)
                    }
                }
            }
        }
    }

    private suspend fun handleValidationResult(result: ValidationResult): Boolean {
        return if (!result.isValid) {
            _channelValidation.send(TransactionValidateEvent.Error(result.message!!))
            false
        } else {
            true
        }
    }

    fun onDateSelected(year: Int, month: Int, dayOfMonth: Int) = viewModelScope.launch {
        val date = DateHelper.formatDate(dayOfMonth, month, year)
        _channel.send(TransactionEvent.DateSelected(date))
    }

    fun onDoneButtonClicked() = viewModelScope.launch {
        _channel.send(TransactionEvent.NavigateBack)
    }

    fun onNewTransactionButtonClicked() = viewModelScope.launch {
        _channel.send(TransactionEvent.ClearFields)
    }
}