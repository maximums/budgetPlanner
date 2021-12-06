package com.endava.budgetplanner.authentication.ui.vm.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endava.budgetplanner.authentication.ui.vm.LoginViewModel
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import com.endava.budgetplanner.common.validators.contracts.Validator
import com.endava.budgetplanner.data.repo.contract.AuthenticationRepository
import com.endava.budgetplanner.di.annotations.EmailValidatorQualifier
import com.endava.budgetplanner.di.annotations.IsNotEmptyValidatorQualifier
import com.endava.budgetplanner.di.annotations.PasswordValidatorQualifier
import javax.inject.Inject

class LoginFactory @Inject constructor(
    private val authenticationRepository: AuthenticationRepository,
    @EmailValidatorQualifier
    private val emailValidator: Validator<CharSequence, Int, Int>,
    @PasswordValidatorQualifier
    private val passwordValidator: Validator<CharSequence, Int, Int>,
    @IsNotEmptyValidatorQualifier
    private val isNotEmptyValidator: MultipleValidator,
    private val tokenPreferences: TokenPreferences
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return LoginViewModel(
            authenticationRepository,
            emailValidator,
            passwordValidator,
            isNotEmptyValidator,
            tokenPreferences
        ) as T
    }
}