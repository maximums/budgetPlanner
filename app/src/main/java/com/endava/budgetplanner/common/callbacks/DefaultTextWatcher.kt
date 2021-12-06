package com.endava.budgetplanner.common.callbacks

import android.text.Editable
import android.text.TextWatcher

class DefaultTextWatcher(private val callback: (String) -> Unit) : TextWatcher {

    override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

    override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
        p0?.let {
            callback(it.toString())
        }
    }

    override fun afterTextChanged(p0: Editable?) {}

}