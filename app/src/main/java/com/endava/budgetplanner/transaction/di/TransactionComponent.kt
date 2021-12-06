package com.endava.budgetplanner.transaction.di

import com.endava.budgetplanner.di.annotations.TransactionScope
import com.endava.budgetplanner.transaction.ui.views.TransactionFragment
import dagger.Subcomponent


@Subcomponent
@TransactionScope
interface TransactionComponent {

    @Subcomponent.Builder
    interface Builder {

        fun create(): TransactionComponent
    }

    fun inject(transactionFragment: TransactionFragment)
}