package com.endava.budgetplanner.authentication.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.budgetplanner.authentication.ui.vm.states.LoginEvent
import com.endava.budgetplanner.authentication.ui.vm.states.LoginState
import com.endava.budgetplanner.common.network.Resource
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import com.endava.budgetplanner.common.validators.contracts.Validator
import com.endava.budgetplanner.data.models.user.UserLogin
import com.endava.budgetplanner.data.repo.contract.AuthenticationRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

private const val BEARER_PREFIX = "Bearer"
private const val UNKNOWN_ERROR = "Unknown error.Try again."

class LoginViewModel(
    private val authenticationRepository: AuthenticationRepository,
    private val emailValidator: Validator<CharSequence, Int, Int>,
    private val passwordValidator: Validator<CharSequence, Int, Int>,
    private val isNotEmptyValidator: MultipleValidator,
    private val tokenPreferences: TokenPreferences
) : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Empty)
    val loginState get() = _loginState.asStateFlow()

    private val _channel = Channel<LoginEvent>()
    val channel get() = _channel.receiveAsFlow()

    fun handleFields(email: String, password: String) {
        _loginState.value = LoginState.ButtonState(isNotEmptyValidator.areValid(email, password))
    }

    fun checkFieldsValidation(email: String, password: String) {
        viewModelScope.launch {
            if (handleValidationResult(emailValidator.getValidationResult(email)) &&
                handleValidationResult(passwordValidator.getValidationResult(password))
            ) {
                login(email, password)
            }
        }
    }

    private suspend fun handleValidationResult(result: ValidationResult): Boolean {
        return if (!result.isValid) {
            _channel.send(LoginEvent.ValidationError(result.message!!))
            false
        } else {
            true
        }
    }

    private fun login(email: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            val user = UserLogin(email, password)
            val resource = authenticationRepository.login(user)
            when (resource) {
                is Resource.Error -> _channel.send(LoginEvent.NetworkError(resource.message))
                is Resource.Success -> {
                    val token = resource.data.webToken
                    val tokenToSend = "$BEARER_PREFIX $token"
                    saveTokenToDataStore(tokenToSend)
                    _channel.send(LoginEvent.NavigateNext)
                }
                Resource.ConnectionError -> _channel.send(LoginEvent.ConnectionError)
                Resource.SuccessEmpty -> _channel.send(LoginEvent.NetworkError(UNKNOWN_ERROR))
            }
            _loginState.value = LoginState.Empty
        }
    }

    private suspend fun saveTokenToDataStore(token: String) {
        tokenPreferences.edit { mutablePreferences ->
            mutablePreferences[TokenPreferences.TOKEN_KEY] = token
        }
    }
}