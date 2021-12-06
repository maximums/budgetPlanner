package com.endava.budgetplanner.data.repo.contract

import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.models.UserBudget

interface DashboardRepository {

    suspend fun getListOfCategories(token: String): Resource<UserBudget>
}