package com.endava.budgetplanner.data.models

data class Transaction(
    val name: String,
    val amount: Double,
    val type: String,
    val category: String,
    val notes: String,
    val date: String
)