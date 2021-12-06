package com.endava.budgetplanner.data.repo

import com.endava.budgetplanner.common.ext.safeNetworkRequest
import com.endava.budgetplanner.common.network.NetworkMonitor
import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.api.ApiService
import com.endava.budgetplanner.data.models.CategoryDetails
import com.endava.budgetplanner.data.models.Transaction
import com.endava.budgetplanner.data.repo.contract.TransactionRepository
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkMonitor: NetworkMonitor
) : TransactionRepository {

    override suspend fun addNewTransaction(
        token: String,
        transaction: Transaction
    ): Resource<Unit> {
        return safeNetworkRequest(networkMonitor) {
            apiService.addNewTransaction(token, transaction)
        }
    }

    override suspend fun getListOfTransactionsByCategory(
        token: String,
        categoryId: Int
    ): Resource<CategoryDetails> {
        return safeNetworkRequest(networkMonitor) {
            apiService.getListOfTransactionsByCategory(token, categoryId)
        }
    }

    override suspend fun deleteTransaction(token: String, transactionId: Int): Resource<Unit> {
        return safeNetworkRequest(networkMonitor) {
            apiService.deleteTransaction(token, transactionId)
        }
    }
}