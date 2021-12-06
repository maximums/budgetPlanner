package com.endava.budgetplanner.authentication.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endava.budgetplanner.authentication.ui.vm.RegisterIndustryViewModel
import com.endava.budgetplanner.common.preferences.LaunchPreferences
import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import com.endava.budgetplanner.common.validators.contracts.Validator
import com.endava.budgetplanner.data.repo.contract.AuthenticationRepository
import com.endava.budgetplanner.di.annotations.IsNotEmptyValidatorQualifier
import com.endava.budgetplanner.di.annotations.NumberValidatorQualifier
import javax.inject.Inject

class RegisterIndustryFactory @Inject constructor(
    private val launchPreferences: LaunchPreferences,
    private val repository: AuthenticationRepository,
    @NumberValidatorQualifier
    private val balanceValidator: Validator<CharSequence,Unit, Unit>,
    @IsNotEmptyValidatorQualifier
    private val isNotEmptyValidator: MultipleValidator
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterIndustryViewModel(
            launchPreferences,
            repository,
            balanceValidator,
            isNotEmptyValidator
        ) as T
    }
}