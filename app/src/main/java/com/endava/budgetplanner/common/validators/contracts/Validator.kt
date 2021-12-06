package com.endava.budgetplanner.common.validators.contracts

import com.endava.budgetplanner.common.utils.ValidationResult


interface Validator<T, MIN, MAX> {
    fun getValidationResult(value: T, min: MIN? = null, max: MAX? = null ): ValidationResult
}
