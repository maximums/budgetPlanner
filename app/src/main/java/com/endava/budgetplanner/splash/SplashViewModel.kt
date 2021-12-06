package com.endava.budgetplanner.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.endava.budgetplanner.common.preferences.LaunchPreferences
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val SPLASH_DELAY = 3000L

class SplashViewModel(
    private val launchPreferences: LaunchPreferences
) : ViewModel() {

    private val _splashState = MutableStateFlow<SplashState>(SplashState.Loading)
    val splashState: StateFlow<SplashState> = _splashState.asStateFlow()

    init {
        configInit()
    }

    private fun configInit() = viewModelScope.launch {
        delay(SPLASH_DELAY)
        val isFirstLaunch = launchPreferences.getData { preferences ->
            preferences[LaunchPreferences.LAUNCH_KEY]
        }.first()
        _splashState.value = if (isFirstLaunch != null)
            SplashState.LoadComplete(true)
        else SplashState.LoadComplete(false)
    }

    class Factory @Inject constructor(
        private val launchPreferences: LaunchPreferences
    ) : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return SplashViewModel(launchPreferences) as T
        }
    }
}