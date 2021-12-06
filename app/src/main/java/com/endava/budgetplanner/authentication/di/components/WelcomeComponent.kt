package com.endava.budgetplanner.authentication.di.components

import com.endava.budgetplanner.authentication.ui.views.WelcomeFragment
import dagger.Subcomponent

@Subcomponent
interface WelcomeComponent {

    @Subcomponent.Builder
    interface Builder {

        fun create(): WelcomeComponent
    }

    fun inject(welcomeFragment: WelcomeFragment)
}