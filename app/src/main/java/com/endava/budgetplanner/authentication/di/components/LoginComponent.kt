package com.endava.budgetplanner.authentication.di.components

import com.endava.budgetplanner.authentication.ui.views.LoginFragment
import com.endava.budgetplanner.di.annotations.SignInScope
import dagger.Subcomponent

@Subcomponent
@SignInScope
interface LoginComponent {

    @Subcomponent.Builder
    interface Builder {

        fun create(): LoginComponent
    }

    fun inject(loginFragment: LoginFragment)
}
