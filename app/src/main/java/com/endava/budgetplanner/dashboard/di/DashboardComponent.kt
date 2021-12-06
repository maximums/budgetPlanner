package com.endava.budgetplanner.dashboard.di

import com.endava.budgetplanner.dashboard.ui.views.DashboardFragment
import com.endava.budgetplanner.di.annotations.DashboardScope
import dagger.Subcomponent

@DashboardScope
@Subcomponent
interface DashboardComponent {

    @Subcomponent.Builder
    interface Builder {

        fun create(): DashboardComponent
    }

    fun inject(dashboardFragment: DashboardFragment)
}