package com.endava.budgetplanner.common.validators

import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.Validator
import javax.inject.Inject

private const val EMAIL_REGEX = "^([a-zA-Z0-9_.\\-]+)@([a-z]{2,}+)\\.([a-z]{2,5})$"
private const val MIN_LENGTH = 8
private const val MAX_LENGTH = 30

class EmailValidator @Inject constructor(
    private val assetsManager: AssetsManager
) : Validator<CharSequence, Int, Int> {

    override fun getValidationResult(value: CharSequence, min: Int?, max: Int?): ValidationResult {
        val regex = EMAIL_REGEX.toRegex()
        return if ((value.length < MIN_LENGTH || value.length > MAX_LENGTH) || !regex.matches(value.trim()))
            ValidationResult(
                false, assetsManager.getResourceString(R.string.validation_error)
            )
        else ValidationResult(isValid = true)
    }
}