package com.endava.budgetplanner

import androidx.test.platform.app.InstrumentationRegistry
import com.endava.budgetplanner.common.utils.AssetsManager
import com.endava.budgetplanner.common.utils.ValidationResult
import com.endava.budgetplanner.common.validators.EmailValidator
import com.endava.budgetplanner.models.EmailTestCase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

@RunWith(Parameterized::class)
class EmailValidatorTest(private val emailTestCase: EmailTestCase) {
    @Test
    fun `check email validation`() {
        val case = EmailValidator(assetManager)
            .getValidationResult(emailTestCase.email)
        assertEquals(emailTestCase.expected, case)
    }

    companion object {
        val assetManager = AssetsManager(InstrumentationRegistry.getInstrumentation().targetContext)
        private val error_message = assetManager.getResourceString(R.string.validation_error)

        @JvmStatic
        @Parameterized.Parameters(name = "{0}-Email {1}-Expected")
        fun setParameters() = listOf(
            EmailTestCase("user@email.com", ValidationResult(true)),
            EmailTestCase("user@email,com", ValidationResult(false, error_message)),
            EmailTestCase("us.er@email.com", ValidationResult(true)),
            EmailTestCase(
                "user@@email.com",
                ValidationResult(false, error_message)
            ),
            EmailTestCase("us_er@email.com", ValidationResult(true)),
            EmailTestCase("us-er@email.com", ValidationResult(true)),
            EmailTestCase("user133@email.com", ValidationResult(true)),
            EmailTestCase(
                "user133@emailcom",
                ValidationResult(false, error_message)
            ),
            EmailTestCase(
                "user133@email..",
                ValidationResult(false, error_message)
            ),
            EmailTestCase("us-er@email.ru", ValidationResult(true)),
            EmailTestCase("us_er@emai.ru", ValidationResult(true)),
            EmailTestCase("user@email..", ValidationResult(false, error_message)),
            EmailTestCase("user133@.gr",ValidationResult(false, error_message)),
            EmailTestCase("user123.endava.com", ValidationResult(false, error_message)),
            EmailTestCase("user123", ValidationResult(false, error_message)),
            EmailTestCase("user123@endavacom.", ValidationResult(false, error_message)),
            EmailTestCase("UserUserUserUserUser@mail.rutor", ValidationResult(false, error_message)),
            EmailTestCase("User@m.ru", ValidationResult(false, error_message)))
    }
}