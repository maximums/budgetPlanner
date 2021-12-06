package com.endava.budgetplanner.data.api

import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.data.models.CategoryDetails
import com.endava.budgetplanner.data.models.Transaction
import com.endava.budgetplanner.data.models.UserBudget
import com.endava.budgetplanner.data.models.user.Token
import com.endava.budgetplanner.data.models.user.User
import com.endava.budgetplanner.data.models.user.UserLogin
import retrofit2.http.*

interface ApiService {

    @POST("api/v1/user/registration")
    suspend fun registerNewUser(@Body user: User): Resource<Unit>

    @POST("api/v1/user/login")
    suspend fun login(@Body userLogin: UserLogin): Resource<Token>

    @GET("api/v1/category-details")
    suspend fun getCategories(@Header("Authorization") token: String): Resource<UserBudget>

    @POST("api/v1/transaction")
    suspend fun addNewTransaction(
        @Header("Authorization") token: String,
        @Body transaction: Transaction
    ): Resource<Unit>

    @GET("api/v1/category-details/{categoryId}")
    suspend fun getListOfTransactionsByCategory(
        @Header("Authorization") token: String,
        @Path("categoryId") categoryId: Int
    ): Resource<CategoryDetails>

    @DELETE("api/v1/transaction/{transactionId}")
    suspend fun deleteTransaction(
        @Header("Authorization") token: String,
        @Path("transactionId") transactionId: Int
    ): Resource<Unit>
}