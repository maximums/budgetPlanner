package com.endava.budgetplanner.data.models.user

import com.google.gson.annotations.SerializedName

data class Token(
    @SerializedName("jwt")
    val webToken: String
)