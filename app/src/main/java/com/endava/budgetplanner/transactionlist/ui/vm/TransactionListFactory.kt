package com.endava.budgetplanner.transactionlist.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.data.repo.contract.TransactionRepository
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class TransactionListFactory @AssistedInject constructor(
    @Assisted private val categoryId: Int,
    private val transactionRepository: TransactionRepository,
    private val tokenPreferences: TokenPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TransactionListViewModel(categoryId, transactionRepository, tokenPreferences) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted categoryId: Int): TransactionListFactory
    }
}