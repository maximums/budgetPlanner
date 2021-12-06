package com.endava.budgetplanner.common.utils

import androidx.annotation.DrawableRes
import com.endava.budgetplanner.R

enum class CategoryProperties(
    val code: String,
    @DrawableRes val imageId: Int
) {
    FOOD("NUTRITION", R.drawable.ic_food),
    ENTERTAINMENT("ENTERTAINMENT", R.drawable.ic_entertainment),
    TAXES("TAXES", R.drawable.ic_taxes),
    SHOPPING("SHOPPING", R.drawable.ic_shopping),
    EX_OTHER("EX_OTHER", R.drawable.ic_other),
    IN_OTHER("IN_OTHER", R.drawable.ic_other),
    SALARIES("PROFIT", R.drawable.ic_salaries),
    INVESTMENTS("INVESTMENT", R.drawable.ic_investments),
    PAYMENTS("PAYMENT", R.drawable.ic_payments),
    SPORT("SPORT", R.drawable.ic_sport),
    UNKNOWN("UNKNOWN", R.drawable.ic_other);

    companion object {
        fun fromCode(code: String?) = values().find { it.code == code } ?: UNKNOWN
    }
}