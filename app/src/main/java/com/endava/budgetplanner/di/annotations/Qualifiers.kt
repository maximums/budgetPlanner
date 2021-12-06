package com.endava.budgetplanner.di.annotations

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class EmailValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class PasswordValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TransNameValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TransDateValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class TransactionAmountValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NameValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class IsNotEmptyValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class NumberValidatorQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class AmountFormatterQualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class RoundFormatterWithPostFixQualifier