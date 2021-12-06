package com.endava.budgetplanner.authentication.ui.vm.states

sealed class LoginState {
    data class ButtonState(val isEnabled: Boolean) : LoginState()
    object Empty : LoginState()
    object Loading : LoginState()
}