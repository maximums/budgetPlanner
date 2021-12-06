package com.endava.budgetplanner.authentication.ui.vm.states

sealed class RegisterState {
    data class ButtonState(val isEnabled: Boolean) : RegisterState()
    object Empty : RegisterState()
}
