package com.endava.budgetplanner.data.repo

import com.endava.budgetplanner.common.ext.safeNetworkRequest
import com.endava.budgetplanner.common.network.NetworkMonitor
import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.api.ApiService
import com.endava.budgetplanner.data.models.user.Token
import com.endava.budgetplanner.data.models.user.User
import com.endava.budgetplanner.data.models.user.UserLogin
import com.endava.budgetplanner.data.repo.contract.AuthenticationRepository
import javax.inject.Inject

class AuthenticationRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val networkMonitor: NetworkMonitor
) : AuthenticationRepository {

    override suspend fun registerNewUser(user: User): Resource<Unit> {
        return safeNetworkRequest(networkMonitor) {
            apiService.registerNewUser(user)
        }
    }

    override suspend fun login(userLogin: UserLogin): Resource<Token> {
        return safeNetworkRequest(networkMonitor) {
            apiService.login(userLogin)
        }
    }
}