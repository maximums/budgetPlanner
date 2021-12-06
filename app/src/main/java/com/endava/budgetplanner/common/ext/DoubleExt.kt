package com.endava.budgetplanner.common.ext



fun Double.truncate() = String.format("%.2f", this).toDouble()

