package com.endava.budgetplanner.di.components

import android.content.Context
import com.endava.budgetplanner.authentication.di.components.LoginComponent
import com.endava.budgetplanner.authentication.di.components.RegisterComponent
import com.endava.budgetplanner.authentication.di.components.WelcomeComponent
import com.endava.budgetplanner.dashboard.di.DashboardComponent
import com.endava.budgetplanner.di.module.AppModule
import com.endava.budgetplanner.splash.di.SplashComponent
import com.endava.budgetplanner.transaction.di.TransactionComponent
import com.endava.budgetplanner.transactionlist.di.TransactionListComponent
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class])
@Singleton
interface AppComponent {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(context: Context): Builder

        fun create(): AppComponent
    }

    fun registerComponent(): RegisterComponent.Builder
    fun splashComponent(): SplashComponent.Builder
    fun loginComponent(): LoginComponent.Builder
    fun welcomeComponent(): WelcomeComponent.Builder
    fun dashboardComponent(): DashboardComponent.Builder
    fun transactionComponent(): TransactionComponent.Builder
    fun transactionListComponent(): TransactionListComponent.Builder
}