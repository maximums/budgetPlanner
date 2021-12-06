package com.endava.budgetplanner.dashboard.ui.vm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.endava.budgetplanner.common.base.AmountFormatter
import com.endava.budgetplanner.common.base.RoundFormatter
import com.endava.budgetplanner.common.preferences.TokenPreferences
import com.endava.budgetplanner.data.repo.contract.DashboardRepository
import com.endava.budgetplanner.di.annotations.AmountFormatterQualifier
import com.endava.budgetplanner.di.annotations.RoundFormatterWithPostFixQualifier
import javax.inject.Inject

class DashboardFactory @Inject constructor(
    private val tokenPreferences: TokenPreferences,
    private val dashboardRepository: DashboardRepository,
    @AmountFormatterQualifier
    private val formatter: AmountFormatter,
    @RoundFormatterWithPostFixQualifier
    private val roundFormatterWithPostFix: RoundFormatter,
    private val roundFormatter: RoundFormatter
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DashboardViewModel(
            tokenPreferences,
            dashboardRepository,
            formatter,
            roundFormatterWithPostFix,
            roundFormatter
        ) as T
    }
}