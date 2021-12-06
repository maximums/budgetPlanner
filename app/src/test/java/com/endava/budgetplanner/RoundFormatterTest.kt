package com.endava.budgetplanner

import com.endava.budgetplanner.common.formatters.RoundFormatterWithPostFix
import com.endava.budgetplanner.common.utils.TransactionType
import com.endava.budgetplanner.models.RoundFormatterCase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class RoundFormatterTest(private val case: RoundFormatterCase) {

    @Test
    fun `check round formatter`() {
        val roundCase = RoundFormatterWithPostFix().format(case.num, case.transactionType)
        assertEquals(roundCase, case.expected)
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{0}-Num {1}-Expected")
        fun setParameters() = listOf(
            RoundFormatterCase(2345.0, TransactionType.INCOME, "2.34k"),
            RoundFormatterCase(2345.0, TransactionType.EXPENSES, "2.35k"),
            RoundFormatterCase(754.875, TransactionType.EXPENSES, "754.88"),
            RoundFormatterCase(78.945, TransactionType.EXPENSES, "78.95"),
            RoundFormatterCase(45.0, TransactionType.EXPENSES, "45.00"),
            RoundFormatterCase(10025.0, TransactionType.EXPENSES, "10.03k"),
            RoundFormatterCase(10025.0, TransactionType.INCOME, "10.02k"),
            RoundFormatterCase(10025.7895, TransactionType.EXPENSES, "10.03k")
        )
    }
}