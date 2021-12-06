package com.endava.budgetplanner.data.models

import android.os.Parcelable
import com.endava.budgetplanner.common.utils.CategoryProperties
import com.endava.budgetplanner.common.utils.TransactionType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MappedCategory(
    val name: String,
    val code: CategoryProperties,
    val color: String,
    val transactionType: TransactionType
) : Parcelable