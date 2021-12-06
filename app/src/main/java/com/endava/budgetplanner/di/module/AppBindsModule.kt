package com.endava.budgetplanner.di.module

import com.endava.budgetplanner.common.base.AmountFormatter
import com.endava.budgetplanner.common.base.RoundFormatter
import com.endava.budgetplanner.common.formatters.AmountFormatterImp
import com.endava.budgetplanner.common.formatters.DefaultRoundFormatter
import com.endava.budgetplanner.common.formatters.RoundFormatterWithPostFix
import com.endava.budgetplanner.common.validators.*
import com.endava.budgetplanner.common.validators.contracts.MultipleValidator
import com.endava.budgetplanner.common.validators.contracts.Validator
import com.endava.budgetplanner.data.repo.AuthenticationRepositoryImpl
import com.endava.budgetplanner.data.repo.DashboardRepositoryImpl
import com.endava.budgetplanner.data.repo.TransactionRepositoryImpl
import com.endava.budgetplanner.data.repo.contract.AuthenticationRepository
import com.endava.budgetplanner.data.repo.contract.DashboardRepository
import com.endava.budgetplanner.data.repo.contract.TransactionRepository
import com.endava.budgetplanner.di.annotations.*
import dagger.Binds
import dagger.Module
import java.util.*

@Module
interface AppBindsModule {

    @Binds
    fun bindAuthenticationRepository(
        authenticationRepositoryImpl: AuthenticationRepositoryImpl
    ): AuthenticationRepository

    @Binds
    fun bindDashboardRepository(
        dashboardRepositoryImpl: DashboardRepositoryImpl
    ): DashboardRepository

    @Binds
    fun bindTransactionRepository(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    @EmailValidatorQualifier
    fun bindEmailValidator(emailValidator: EmailValidator): Validator<CharSequence, Int, Int>

    @Binds
    @PasswordValidatorQualifier
    fun bindPasswordValidator(passwordValidator: PasswordValidator): Validator<CharSequence, Int, Int>

    @Binds
    @NameValidatorQualifier
    fun bindNameValidator(nameValidator: NameValidator): Validator<CharSequence, Int, Int>

    @Binds
    @TransDateValidatorQualifier
    fun bindTransDateValidator(bind: TransactionDateValidator): Validator<Date, Date, Date>

    @Binds
    @TransNameValidatorQualifier
    fun bindTransactionNameValidator(bind: TransactionTitleValidator): Validator<CharSequence, Int, Int>

    @Binds
    @NumberValidatorQualifier
    fun bindBalanceValidator(numberValidator: BalanceValidator): Validator<CharSequence, Unit, Unit>

    @Binds
    @TransactionAmountValidatorQualifier
    fun bindTransactionAmountValidator(bind: TransactionAmountValidator): Validator<Double, Double, Double>

    @Binds
    @IsNotEmptyValidatorQualifier
    fun bindIsNotEmptyValidator(isNotEmptyValidator: IsNotEmptyValidator): MultipleValidator

    @Binds
    @AmountFormatterQualifier
    fun bindAmountFormatter(formatter: AmountFormatterImp): AmountFormatter

    @Binds
    @RoundFormatterWithPostFixQualifier
    fun bindRoundFormatterWithPostFix(roundFormatterWithPostFix: RoundFormatterWithPostFix): RoundFormatter

    @Binds
    fun bindRoundFormatter(defaultRoundFormatter: DefaultRoundFormatter): RoundFormatter
}