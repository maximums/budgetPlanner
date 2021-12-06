package com.endava.budgetplanner.common.validators.contracts

interface MultipleValidator {

    fun areValid(vararg field: CharSequence): Boolean
}