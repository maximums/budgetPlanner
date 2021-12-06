package com.endava.budgetplanner

import android.app.Application
import com.endava.budgetplanner.di.components.AppComponent
import com.endava.budgetplanner.di.components.DaggerAppComponent

class BudgetPlannerApp : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .create()
    }
}