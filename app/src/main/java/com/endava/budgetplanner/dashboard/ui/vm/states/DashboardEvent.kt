package com.endava.budgetplanner.dashboard.ui.vm.states

import androidx.annotation.StringRes
import com.endava.budgetplanner.data.models.MappedCategory

sealed class DashboardEvent {
    data class Error(val message: String) : DashboardEvent()
    data class ErrorId(@StringRes val textId: Int) : DashboardEvent()
    object ConnectionError : DashboardEvent()
}
