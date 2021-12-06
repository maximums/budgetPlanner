package com.endava.budgetplanner

import androidx.test.platform.app.InstrumentationRegistry
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.PasswordValidator
import com.endava.budgetplanner.models.PasswordTestCase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class PasswordValidatorTest(private val passwordTestCase: PasswordTestCase) {

    @Test
    fun `check email validation`() {
        val case = PasswordValidator(assetManager).getValidationResult(passwordTestCase.password)
        assertEquals(passwordTestCase.expected, case)
    }

    companion object {
        val assetManager = AssetsManager(InstrumentationRegistry.getInstrumentation().targetContext)
        val error_message = assetManager.getResourceString(R.string.validation_error)

        @JvmStatic
        @Parameterized.Parameters(name = "{0}-Password {1}-Expected")
        fun setParameters() = listOf(
            PasswordTestCase("Endava#2021", ValidationResult(true)),
            PasswordTestCase("hello", ValidationResult(false, error_message)),
            PasswordTestCase("EndavaEndava", ValidationResult(false, error_message)),
            PasswordTestCase("endava20211234567890123", ValidationResult(false, error_message)),
            PasswordTestCase("Endava$2021", ValidationResult(true)),
            PasswordTestCase("Endava!@#$%_-", ValidationResult(false, error_message)),
            PasswordTestCase("Endava^^", ValidationResult(false, error_message)),
            PasswordTestCase("*/*/*", ValidationResult(false, error_message)),
            PasswordTestCase("Endava#\$Endava", ValidationResult(false, error_message)),
            PasswordTestCase("Endava#2021.", ValidationResult(true))
        )
    }
}