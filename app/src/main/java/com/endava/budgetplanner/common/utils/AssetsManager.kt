package com.endava.budgetplanner.common.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class AssetsManager @Inject constructor(private val context: Context) {

    fun getResourceString(@StringRes idRes: Int) =
        context.resources.getString(idRes)

    fun getResourceString(@StringRes idRes: Int, value: String?) =
        context.resources.getString(idRes, value)
}