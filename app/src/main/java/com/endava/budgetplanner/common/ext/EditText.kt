package com.endava.budgetplanner.common.ext

import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import androidx.core.widget.doOnTextChanged

private const val DELIMITER = "."
private const val INDEX_SHIFT = 1

fun EditText.decimalLimit(decimals: Int) {
    this.doOnTextChanged { text, _, _, _ ->
        val intPartBuilder = StringBuilder()
        val decimalPartBuilder = StringBuilder()
        text?.let {
            if (text.length >= decimals + INDEX_SHIFT ) {
                val decimalSplit= text.split(DELIMITER)
                if (decimalSplit.size == decimals) {
                    val intPart = decimalSplit[0]
                    val decimalPart = decimalSplit[1]
                    if (decimalPart.length > decimals) {
                        decimalPart.forEachIndexed { index, char ->
                            if (index <= INDEX_SHIFT) {
                                decimalPartBuilder.append(char)
                            } else {
                                return@forEachIndexed
                            }
                        }
                        intPartBuilder.append(intPart).append(DELIMITER).append(decimalPartBuilder)
                        this.setText(intPartBuilder.toString())
                        this.setSelection(intPartBuilder.length)
                    }
                }
            }
        }
    }
}

fun EditText.lengthLimit(limit: Int) {
    this.doAfterTextChanged {
        val count = it?.length ?: 0
        val newValue = StringBuilder()
        if (count > limit) {
            it?.forEachIndexed { index, char ->
                if (index <= limit - INDEX_SHIFT) {
                    newValue.append(char)
                } else {
                    return@forEachIndexed
                }
            }
            this.setText(newValue.toString())
            this.setSelection(newValue.length)
        }
    }
}