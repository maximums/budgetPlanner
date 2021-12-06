package com.endava.budgetplanner.di.annotations

import javax.inject.Scope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SignInScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class RegisterScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class SplashScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class WelcomeScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class DashboardScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class TransactionScope

@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class TransactionListScope