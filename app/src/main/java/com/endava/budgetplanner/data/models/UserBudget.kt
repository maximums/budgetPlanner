package com.endava.budgetplanner.data.models

import com.google.gson.annotations.SerializedName

data class UserBudget(
    val currentAmount: Double,
    @SerializedName("categoryDetailsList")
    val categories: List<Category>
)