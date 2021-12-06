package com.endava.budgetplanner.authentication.ui.vm.states

sealed class RegisterIndustryState {
    object Empty : RegisterIndustryState()
    object Loading : RegisterIndustryState()
    data class ButtonState(val isEnabled: Boolean) : RegisterIndustryState()
    data class BalanceFieldState(
        val addPrefix: Boolean,
        val mixSize: Boolean,
        val text: String
    ) : RegisterIndustryState()
}
