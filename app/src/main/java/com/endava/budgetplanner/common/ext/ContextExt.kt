package com.endava.budgetplanner.common.ext

import android.content.Context
import com.endava.budgetplanner.BudgetPlannerApp
import com.endava.budgetplanner.di.components.AppComponent

fun Context.provideAppComponent(): AppComponent = when (this) {
    is BudgetPlannerApp -> this.appComponent
    else -> this.applicationContext.provideAppComponent()
}