package com.endava.budgetplanner.common.validators

import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.Validator
import javax.inject.Inject

private const val NAME_REGEX = "[A-Za-z0-9\\s]{5,25}"
private const val MIN_LENGTH = 5
private const val MAX_LENGTH = 25

class TransactionTitleValidator @Inject constructor(
    private val assetsManager: AssetsManager
) : Validator<CharSequence, Int, Int> {
    override fun getValidationResult(value: CharSequence, min: Int?, max: Int?): ValidationResult {
        val regex = NAME_REGEX.toRegex()
        val newMin = min ?: MIN_LENGTH
        val newMax = max ?: MAX_LENGTH
        return when {
            value.length < newMin -> {
                ValidationResult(false,
                    assetsManager.getResourceString(
                        R.string.error_less_than_min_char, newMin.toString()
                    )
                )
            }
            value.length > newMax -> ValidationResult(false,
                assetsManager.getResourceString(
                    R.string.error_more_than_max_char, newMax.toString()
                )
            )
            !regex.matches(value.trim()) -> ValidationResult(false,
                assetsManager.getResourceString(
                    R.string.error_special_char
                )
            )
            else -> ValidationResult(isValid = true)
        }
    }
}