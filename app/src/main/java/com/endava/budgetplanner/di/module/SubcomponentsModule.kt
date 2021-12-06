package com.endava.budgetplanner.di.module

import com.endava.budgetplanner.authentication.di.components.LoginComponent
import com.endava.budgetplanner.authentication.di.components.RegisterComponent
import com.endava.budgetplanner.authentication.di.components.WelcomeComponent
import com.endava.budgetplanner.dashboard.di.DashboardComponent
import com.endava.budgetplanner.splash.di.SplashComponent
import com.endava.budgetplanner.transaction.di.TransactionComponent
import com.endava.budgetplanner.transactionlist.di.TransactionListComponent
import dagger.Module

@Module(
    subcomponents = [
        RegisterComponent::class,
        LoginComponent::class,
        SplashComponent::class,
        WelcomeComponent::class,
        DashboardComponent::class,
        TransactionComponent::class,
        TransactionListComponent::class
    ]
)
object SubcomponentsModule