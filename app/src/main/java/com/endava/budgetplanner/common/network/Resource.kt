package com.endava.budgetplanner.common.network

sealed class Resource<out T> {
    object ConnectionError : Resource<Nothing>()
    object SuccessEmpty : Resource<Nothing>()
    data class Success<out T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}