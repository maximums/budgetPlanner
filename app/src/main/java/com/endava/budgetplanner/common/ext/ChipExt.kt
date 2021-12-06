package com.endava.budgetplanner.common.ext

import android.content.res.ColorStateList
import android.graphics.Color
import com.google.android.material.chip.Chip

fun Chip.transactionCategoryChip(
    title: String,
    categoryCode: String,
    color: String,
) {
    val state = arrayOf(
        intArrayOf(-android.R.attr.state_checked),
        intArrayOf(android.R.attr.state_checked)
    )
    val colorState =
        ColorStateList(
            state,
            intArrayOf(Color.parseColor(color.addAlpha()), Color.parseColor(color))
        )
    chipBackgroundColor = colorState
    tag = categoryCode
    text = title
}