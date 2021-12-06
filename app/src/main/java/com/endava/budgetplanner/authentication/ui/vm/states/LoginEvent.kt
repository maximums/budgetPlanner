package com.endava.budgetplanner.authentication.ui.vm.states

sealed class LoginEvent {
    data class NetworkError(val message: String) : LoginEvent()
    data class ValidationError(val message: String) : LoginEvent()
    object NavigateNext : LoginEvent()
    object ConnectionError : LoginEvent()
}