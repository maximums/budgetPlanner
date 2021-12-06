package com.endava.budgetplanner.splash

sealed class SplashState {
    object Loading : SplashState()
    data class LoadComplete(val navigateToLogin: Boolean) : SplashState()
}
