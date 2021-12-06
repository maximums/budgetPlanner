package com.endava.budgetplanner.common.network

import androidx.annotation.StringRes
import com.endava.budgetplanner.R

enum class StatusCode(val code: Int, @StringRes val textId: Int?) {
    CONFLICT(409, R.string.conflict_error),
    BAD_REQUEST(400, R.string.bad_request_error),
    INVALID_CREDENTIALS(401, R.string.invalid_password_email_error),
    NO_CONTENT(204, null),
    OK(200, null)
}