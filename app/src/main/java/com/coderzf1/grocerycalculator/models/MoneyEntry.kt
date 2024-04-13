package com.coderzf1.grocerycalculator.models

import com.coderzf1.grocerycalculator.presentation.EntryType
import java.math.BigDecimal
import java.util.UUID

data class MoneyEntry(
    val amount: BigDecimal = BigDecimal.ZERO,
    val qty: BigDecimal = BigDecimal.ONE,
    val entryType: EntryType,
    val id:String = UUID.randomUUID().toString()
)