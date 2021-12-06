package com.endava.budgetplanner.data.repo

import com.endava.budgetplanner.common.ext.safeNetworkRequest
import com.endava.budgetplanner.common.network.NetworkMonitor
import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.api.ApiService
import com.endava.budgetplanner.data.models.UserBudget
import com.endava.budgetplanner.data.repo.contract.DashboardRepository
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkMonitor: NetworkMonitor
) : DashboardRepository {

    override suspend fun getListOfCategories(token: String): Resource<UserBudget> {
        return safeNetworkRequest(networkMonitor) {
            apiService.getCategories(token)
        }
    }
}