package com.endava.budgetplanner.dashboard.ui.vm.states

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.endava.budgetplanner.data.models.AmountBalance
import com.endava.budgetplanner.data.models.Category
import com.endava.budgetplanner.data.models.MappedCategory
import com.github.mikephil.charting.data.PieEntry

sealed class DashboardState {
    data class Success(
        val pos: Int,
        val list: List<Category>,
        val amount: AmountBalance,
        val cardAmount: String,
        val pieChartAmount: String,
        @StringRes val templateAmount: Int,
        @DrawableRes val cardImg: Int,
        val pieEntries: List<PieEntry>,
        val viewpagerAdapterPos: Int,
        val mappedCategories: List<MappedCategory>
    ) : DashboardState()

    data class NoTransactions(
        val pos: Int,
        @StringRes val textId: Int,
        val amount: AmountBalance,
        @StringRes val initialAmount: Int,
        @DrawableRes val cardImg: Int,
        val viewpagerAdapterPos: Int,
        val mappedCategories: List<MappedCategory>
    ) : DashboardState()

    object Empty : DashboardState()
    object Loading : DashboardState()
}