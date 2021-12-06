package com.endava.budgetplanner.common.ext

import android.text.SpannableString
import android.text.style.AbsoluteSizeSpan

private const val DEFAULT_START_INDEX = 0
private const val UNDEFINED_INDEX = -1
private const val TRANSPARENCY_20 = "#32"
private const val HEX_COLOR_PREFIX = "#"

fun String.toMixedSize(
    absoluteSize: Int,
    size: Int,
    breakpoint: Char,
    startIndex: Int = DEFAULT_START_INDEX
): SpannableString {
    val textLength = length
    val posOfBreakPoint = if (this.indexOf(breakpoint) == UNDEFINED_INDEX) {
        textLength
    } else {
        this.indexOf(breakpoint)
    }
    val span = SpannableString(this).apply {
        setSpan(
            AbsoluteSizeSpan(absoluteSize),
            startIndex,
            posOfBreakPoint,
            SpannableString.SPAN_INCLUSIVE_INCLUSIVE
        )
    }
    return if (posOfBreakPoint != textLength) {
        span.apply {
            setSpan(
                AbsoluteSizeSpan(size),
                posOfBreakPoint,
                textLength,
                SpannableString.SPAN_INCLUSIVE_INCLUSIVE
            )
        }
    } else span
}

fun String.addAlpha() = "$TRANSPARENCY_20${removePrefix(HEX_COLOR_PREFIX)}"