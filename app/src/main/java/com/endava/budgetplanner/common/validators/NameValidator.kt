package com.endava.budgetplanner.common.validators

import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.Validator
import javax.inject.Inject

private const val NAME_REGEX = "^[A-Za-z]{3,22}$"
private const val MIN_LENGTH = 3
private const val MAX_LENGTH = 22

class NameValidator @Inject constructor(
    private val assetsManager: AssetsManager
) : Validator<CharSequence, Int, Int> {

    override fun getValidationResult(value: CharSequence, min: Int?, max: Int?): ValidationResult {
        val regex = NAME_REGEX.toRegex()
        return if (value.length < MIN_LENGTH || value.length > MAX_LENGTH)
            ValidationResult(
                false, assetsManager.getResourceString(R.string.name_length_error)
            )
        else if (!regex.matches(value.trim())){
            ValidationResult(
                false, assetsManager.getResourceString(R.string.name_validation_error)
            )
        }
        else ValidationResult(isValid = true)
    }
}