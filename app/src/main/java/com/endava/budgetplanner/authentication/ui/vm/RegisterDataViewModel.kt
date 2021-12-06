package com.endava.budgetplanner.authentication.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterEvent
import com.endava.budgetplanner.authentication.ui.vm.states.RegisterState
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import com.endava.budgetplanner.common.validators.contracts.Validator
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

class RegisterDataViewModel(
    private val nameValidator: Validator<CharSequence, Int, Int>,
    private val isNotEmptyValidator: MultipleValidator
) : ViewModel() {

    private val _state = MutableStateFlow<RegisterState>(RegisterState.Empty)
    val state get() = _state.asStateFlow()

    private val _channel = Channel<RegisterEvent>()
    val channel get() = _channel.receiveAsFlow()

    fun handleFields(name: String, lastName: String) {
        _state.value = RegisterState.ButtonState(isNotEmptyValidator.areValid(name, lastName))
    }

    fun checkFieldsValidation(name: String, lastName: String) {
        viewModelScope.launch {
            if (handleValidationResult(nameValidator.getValidationResult(name)) &&
                handleValidationResult(nameValidator.getValidationResult(lastName))
            ) {
                _channel.send(RegisterEvent.NavigateNext)
            }
        }
    }

    private suspend fun handleValidationResult(result: ValidationResult): Boolean {
        return if (!result.isValid) {
            _channel.send(RegisterEvent.Error(result.message!!))
            false
        } else {
            true
        }
    }
}