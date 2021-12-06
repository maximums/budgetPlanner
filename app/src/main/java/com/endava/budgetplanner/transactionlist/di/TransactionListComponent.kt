package com.endava.budgetplanner.transactionlist.di

import com.endava.budgetplanner.di.annotations.TransactionListScope
import com.endava.budgetplanner.transactionlist.ui.views.TransactionListFragment
import dagger.Subcomponent

@Subcomponent
@TransactionListScope
interface TransactionListComponent {

    @Subcomponent.Builder
    interface Builder {

        fun create(): TransactionListComponent
    }

    fun inject(transactionFragment: TransactionListFragment)
}