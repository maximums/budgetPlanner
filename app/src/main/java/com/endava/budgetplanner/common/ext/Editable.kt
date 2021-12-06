package com.endava.budgetplanner.common.ext

import android.text.Editable

private const val DEFAULT_DOUBLE = 0.0

fun Editable.toDoubleOrEmpty(): Double {
    var convert = DEFAULT_DOUBLE
    if (this.isNotEmpty()) { convert = this.toString().toDouble() }
    return convert
}