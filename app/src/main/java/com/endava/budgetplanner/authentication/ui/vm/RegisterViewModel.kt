package com.endava.budgetplanner.authentication.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.budgetplanner.R
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterEvent
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterState
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import com.endava.budgetplanner.common.validators.contracts.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val emailValidator: Validator<CharSequence, Int, Int>,
    private val passwordValidator: Validator<CharSequence, Int, Int>,
    private val isNotEmptyValidator: MultipleValidator,
    private val assetsManager: AssetsManager,
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Empty)
    val state get() = _state.asStateFlow()

    private val _channel = Channel<RegisterEvent>()
    val channel get() = _channel.receiveAsFlow()

    fun handleFields(email: String, password: String, pasConf: String) {
        _state.value =
            RegisterState.ButtonState(isNotEmptyValidator.areValid(email, password, pasConf))
    }

    fun checkFieldsValidation(email: String, password: String, pasConf: String) {
        viewModelScope.launch {
            if (handleValidationResult(emailValidator.getValidationResult(email)) &&
                handleValidationResult(passwordValidator.getValidationResult(password))
            ) {
                if (password == pasConf) {
                    _channel.send(RegisterEvent.NavigateNext)
                } else {
                    _channel.send(RegisterEvent.Error(assetsManager.getResourceString(R.string.password_not_matching)))
                }
            }
        }
    }

    private suspend fun handleValidationResult(validationResult: ValidationResult): Boolean {
        return if (!validationResult.isValid) {
                _channel.send(RegisterEvent.Error(validationResult.message!!))
                false
            } else true
        }
}