package com.endava.budgetplanner.transactionlist.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.common.utils.CurrencyType
import com.endava.budgetplanner.data.models.CategoryDetails
import com.endava.budgetplanner.data.models.TransactionResponse
import com.endava.budgetplanner.data.repo.contract.TransactionRepository
import com.endava.budgetplanner.transactionlist.ui.vm.state.TransactionListEvent
import com.endava.budgetplanner.transactionlist.ui.vm.state.TransactionListState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class TransactionListViewModel(
    private val categoryId: Int,
    private val transactionRepository: TransactionRepository,
    private val tokenPreferences: TokenPreferences
) : ViewModel() {

    init {
        getListOfTransactionsByCategoryId()
    }

    private val _state = MutableStateFlow<TransactionListState>(TransactionListState.Empty)
    val state get() = _state.asStateFlow()

    private val _channel = Channel<TransactionListEvent>()
    val channel get() = _channel.receiveAsFlow()

    private suspend fun getToken(): String? {
        return tokenPreferences.getData { preferences ->
            preferences[TokenPreferences.TOKEN_KEY]
        }.first()
    }

    fun getListOfTransactionsByCategoryId() = viewModelScope.launch {
        val token = getToken()
        if (token != null) {
            _state.value = TransactionListState.Loading
            val resource = transactionRepository.getListOfTransactionsByCategory(token, categoryId)
            when (resource) {
                Resource.ConnectionError -> {
                    _channel.send(TransactionListEvent.ConnectionError)
                    _state.value = TransactionListState.Empty
                }
                is Resource.Error -> {
                    _channel.send(TransactionListEvent.Error(resource.message))
                    _state.value = TransactionListState.Empty
                }
                is Resource.Success -> handleSuccessResponse(resource.data)
                Resource.SuccessEmpty -> {
                    _channel.send(TransactionListEvent.ErrorId(R.string.unknown_error))
                    _state.value = TransactionListState.Empty
                }
            }
        } else {
            _channel.send(TransactionListEvent.ErrorId(R.string.invalid_token_error))
        }
    }

    fun deleteTransaction(transactions: List<TransactionResponse>, posToDelete: Int) {
        viewModelScope.launch {
            val token = getToken()
            if (token != null) {
                _state.value = TransactionListState.Loading
                val transaction = transactions[posToDelete]
                val resource = transactionRepository.deleteTransaction(token, transaction.id)
                when (resource) {
                    Resource.ConnectionError -> {
                        _channel.send(TransactionListEvent.ConnectionError)
                        _state.value = TransactionListState.Empty
                    }
                    is Resource.Error -> {
                        _channel.send(TransactionListEvent.Error(resource.message))
                        _state.value = TransactionListState.Empty
                    }
                    is Resource.Success -> {
                        _channel.send(TransactionListEvent.ErrorId(R.string.unknown_error_try_again))
                    }
                    Resource.SuccessEmpty -> getListOfTransactionsByCategoryId()
                }
            } else {
                _channel.send(TransactionListEvent.ErrorId(R.string.invalid_token_error))
            }
        }
    }

    private fun handleSuccessResponse(categoryDetails: CategoryDetails) {
        val transactions = categoryDetails.transactions
        val name = categoryDetails.name
        val amount = CurrencyType.DOLLAR.sign.plus(categoryDetails.amount)
        _state.value = TransactionListState.Success(
            mapTransactions(transactions),
            name,
            amount
        )
    }

    private fun mapTransactions(
        list: List<TransactionResponse>
    ): List<TransactionResponse> {
        return list.map { response ->
            TransactionResponse(
                response.id,
                response.name,
                CurrencyType.DOLLAR.sign.plus(response.amount),
                response.date
            )
        }
    }
}