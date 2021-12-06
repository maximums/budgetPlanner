package com.endava.budgetplanner.data.models

data class ErrorBody(
    val statusCode: Int,
    val timestamp: String,
    val message: String,
    val description: String
)