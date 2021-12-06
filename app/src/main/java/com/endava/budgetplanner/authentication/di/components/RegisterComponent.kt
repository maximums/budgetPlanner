package com.endava.budgetplanner.authentication.di.components

import com.endava.budgetplanner.authentication.ui.views.RegisterDataFragment
import com.endava.budgetplanner.authentication.ui.views.RegisterDataIndustryFragment
import com.endava.budgetplanner.authentication.ui.views.RegisterFragment
import com.endava.budgetplanner.di.annotations.RegisterScope
import dagger.Subcomponent

@Subcomponent
@RegisterScope
interface RegisterComponent {

    @Subcomponent.Builder
    interface Builder {

        fun create(): RegisterComponent
    }

    fun inject(registerFragment: RegisterFragment)
    fun inject(registerFragment: RegisterDataFragment)
    fun inject(registerDataIndustryFragment: RegisterDataIndustryFragment)
}