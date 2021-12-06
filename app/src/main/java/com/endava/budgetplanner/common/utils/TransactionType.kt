package com.endava.budgetplanner.common.utils

enum class TransactionType(val typeName: String) {
        EXPENSES("EXPENSE"), INCOME("INCOME"), UNKNOWN("UNKNOWN");

    companion object {
        fun fromType(type: String?) = values().find { it.typeName == type } ?: UNKNOWN
    }
}