package com.endava.budgetplanner

import com.endava.budgetplanner.common.validators.IsNotEmptyValidator
import com.endava.budgetplanner.models.IsNotEmptyTestCase
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class IsNotEmptyValidatorTest(private val isNotEmptyTestCase: IsNotEmptyTestCase) {

    @Test
    fun `check it fields aren't empty`() {
        val case = IsNotEmptyValidator().areValid(*isNotEmptyTestCase.fields.toTypedArray())
        assertEquals(case, isNotEmptyTestCase.expected)
    }

    companion object {

        @JvmStatic
        @Parameterized.Parameters(name = "{0} - Fields {1} - Expected")
        fun setParameters() = listOf(
            IsNotEmptyTestCase(listOf("  ", "Hello"), false),
            IsNotEmptyTestCase(listOf("Hello", "Hey"), true),
            IsNotEmptyTestCase(listOf("  ", "  "), false),
            IsNotEmptyTestCase(listOf("h", "e"), true),
            IsNotEmptyTestCase(listOf("  Hello", "Hey  "), true),
            IsNotEmptyTestCase(listOf("", ""), false),
            IsNotEmptyTestCase(listOf("hey  ", "  hey"), true),
            IsNotEmptyTestCase(listOf("  ", "Hello"), false)
        )
    }
}