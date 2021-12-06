package com.endava.budgetplanner.common.validators

import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import javax.inject.Inject

private const val EMPTY_REGEX = "^(?!\\s*\$).+"

class IsNotEmptyValidator @Inject constructor() : MultipleValidator {

    override fun areValid(vararg field: CharSequence): Boolean {
        val regex = EMPTY_REGEX.toRegex()
        return field.all { regex.matches(it) }
    }
}