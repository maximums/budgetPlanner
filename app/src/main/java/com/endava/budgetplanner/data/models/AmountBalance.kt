package com.endava.budgetplanner.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AmountBalance (
    val category: String,
    val balance: Double,
) : Parcelable