package com.endava.budgetplanner.common.validators

import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.Validator
import javax.inject.Inject

private const val BALANCE_REGEX = "^\\d{1,5}(\\.(\\d{1,2}))?$"
private const val MIN_BALANCE = 0
private const val MAX_BALANCE = 10000

class BalanceValidator @Inject constructor(
    private val assetsManager: AssetsManager
) : Validator<CharSequence, Unit, Unit> {

    override fun getValidationResult(value: CharSequence, min: Unit?, max: Unit?): ValidationResult {
        val regex = BALANCE_REGEX.toRegex()
        val balance = value.toString().toDouble()
        return if (balance > MIN_BALANCE && balance <= MAX_BALANCE && regex.matches(value))
            ValidationResult(isValid = true)
        else if (balance > MAX_BALANCE)
            ValidationResult(
                false, assetsManager.getResourceString(R.string.number_validation_error)
            )
        else ValidationResult(
            false, assetsManager.getResourceString(R.string.validation_error)
        )

    }
}