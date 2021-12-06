package com.endava.budgetplanner.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.*

@Parcelize
data class DateFormatter(
    val date: Date,
    val formatted: String,
) : Parcelable