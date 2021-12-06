package com.endava.budgetplanner.data.models.user

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val firstName: String? = null,
    val lastName: String? = null,
    val password: String? = null,
    val industry: String? = null,
    @SerializedName("username")
    val email: String? = null,
    val initialBalance: Double? = null
) : Parcelable