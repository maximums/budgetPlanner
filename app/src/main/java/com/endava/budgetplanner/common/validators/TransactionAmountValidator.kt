package com.endava.budgetplanner.common.validators

import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.Validator
import javax.inject.Inject

private const val MIN_BALANCE = 1.00
private const val MAX_BALANCE = Double.MAX_VALUE

class TransactionAmountValidator @Inject constructor(
    private val assetsManager: AssetsManager
) : Validator<Double, Double, Double> {
    override fun getValidationResult(value: Double, min: Double?, max: Double?): ValidationResult {
        val newMin = min ?: MIN_BALANCE
        val newMax = max ?: MAX_BALANCE
        return when {
            value < newMin -> {
                ValidationResult(
                    false,
                    assetsManager.getResourceString(
                        R.string.error_amount_less_than_min, MIN_BALANCE.toString()
                    )
                )
            }
            value > newMax -> {
                ValidationResult(
                    false,
                    assetsManager.getResourceString(
                        R.string.error_amount_more_than_max, newMax.toString()
                    )
                )
            }
            else -> ValidationResult(isValid = true)
        }
    }
}