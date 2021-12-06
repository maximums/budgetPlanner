package com.endava.budgetplanner.data.repo.contract

import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.models.user.Token
import com.endava.budgetplanner.data.models.user.User
import com.endava.budgetplanner.data.models.user.UserLogin

interface AuthenticationRepository {

    suspend fun registerNewUser(user: User): Resource<Any>

    suspend fun login(userLogin: UserLogin): Resource<Token>
}