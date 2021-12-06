package com.endava.budgetplanner.common.validators

import com.endava.budgetplanner.R
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.DateHelper
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.Validator
import java.util.*
import javax.inject.Inject

class TransactionDateValidator @Inject constructor(
    private val assetsManager: AssetsManager
) : Validator<Date, Date, Date> {
    private val currentDay = DateHelper.getTomorrowDay()
    private val dateYearAgo = DateHelper.getYearAgo()

    override fun getValidationResult(value: Date, min: Date?, max: Date?): ValidationResult {
        val newMin = min?.let { DateHelper.formatDate(it) } ?: currentDay
        val newMax = max?.let { DateHelper.formatDate(it) } ?: dateYearAgo
        return when {
            value >= newMin.date -> {
                ValidationResult(false,
                    assetsManager.getResourceString(
                        R.string.error_trans_date_less_than_min
                    )
                )
            }
            value <= newMax.date -> {
                ValidationResult(false,
                    assetsManager.getResourceString(
                        R.string.error_trans_ate_more_than_max
                    )
                )
            }
            else -> ValidationResult(isValid = true)
        }
    }

}