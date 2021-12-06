package com.endava.budgetplanner

import androidx.test.platform.app.InstrumentationRegistry
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.NameValidator
import com.endava.budgetplanner.models.NameTestCase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class NameValidatorTest(private val nameTestCase: NameTestCase) {

    @Test
    fun `check name validation`() {
        val case = NameValidator(assetManager).getValidationResult(nameTestCase.name)
        assertEquals(case, nameTestCase.expected)
    }

    companion object {
        val assetManager = AssetsManager(InstrumentationRegistry.getInstrumentation().targetContext)

        @JvmStatic
        @Parameterized.Parameters(name = "{0}-Name {1}-Expected")
        fun setParameters() = listOf(
            NameTestCase("Alex", ValidationResult(true)),
            NameTestCase("Al", ValidationResult(false, assetManager.getResourceString(R.string.name_length_error))),
            NameTestCase(
                "UserUserUserUserUserUse",
                ValidationResult(false, assetManager.getResourceString(R.string.name_length_error))
            ),
            NameTestCase("UserUserUserUserUserUs", ValidationResult(true)),
            NameTestCase("USER", ValidationResult(true)),
            NameTestCase(".User.", ValidationResult(false, assetManager.getResourceString(R.string.name_validation_error))),
            NameTestCase("User123", ValidationResult(false, assetManager.getResourceString(R.string.name_validation_error))),
            NameTestCase("useR", ValidationResult(true)),
            NameTestCase("UsEr", ValidationResult(true))
        )
    }
}