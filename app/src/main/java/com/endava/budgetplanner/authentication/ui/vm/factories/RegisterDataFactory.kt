package com.endava.budgetplanner.authentication.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endava.budgetplanner.authentication.ui.vm.RegisterDataViewModel
import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import com.endava.budgetplanner.common.validators.contracts.Validator
import com.endava.budgetplanner.di.annotations.IsNotEmptyValidatorQualifier
import com.endava.budgetplanner.di.annotations.NameValidatorQualifier
import javax.inject.Inject

class RegisterDataFactory @Inject constructor(
    @NameValidatorQualifier
    private val nameValidator: Validator<CharSequence, Int, Int>,
    @IsNotEmptyValidatorQualifier
    private val isNotEmptyValidator: MultipleValidator
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RegisterDataViewModel(nameValidator, isNotEmptyValidator) as T
    }
}