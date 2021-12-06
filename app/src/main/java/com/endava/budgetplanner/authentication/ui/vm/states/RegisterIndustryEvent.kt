package com.endava.budgetplanner.authentication.ui.vm.states

import androidx.annotation.StringRes

sealed class RegisterIndustryEvent {
    data class Error(val message: String) : RegisterIndustryEvent()
    data class ValidationError(val message: String) : RegisterIndustryEvent()
    object NavigateNext : RegisterIndustryEvent()
    object ConnectionError : RegisterIndustryEvent()
}