package com.endava.budgetplanner.splash.di

import com.endava.budgetplanner.di.annotations.SplashScope
import com.endava.budgetplanner.splash.SplashActivity
import dagger.Subcomponent

@Subcomponent
@SplashScope
interface SplashComponent {

    @Subcomponent.Builder
    interface Builder {

        fun create(): SplashComponent
    }

    fun inject(splashActivity: SplashActivity)
}