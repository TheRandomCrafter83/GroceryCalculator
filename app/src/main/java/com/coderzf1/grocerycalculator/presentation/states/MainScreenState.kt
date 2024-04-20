package com.coderzf1.grocerycalculator.presentation.states

import com.coderzf1.grocerycalculator.models.MoneyEntry
import java.math.BigDecimal

data class MainScreenState(
    val entry:List<BigDecimal> = emptyList(),
    val quantity:BigDecimal = BigDecimal.ONE,
    val entries:List<MoneyEntry> = emptyList(),
    val subtotal:BigDecimal = BigDecimal.ZERO,
    val total:BigDecimal = BigDecimal.ZERO,
    val foodTax:BigDecimal = BigDecimal(.03),
    val nonfoodTax:BigDecimal = BigDecimal(.07),
    val isFoodStamp:Boolean = false,
    val totalTax:BigDecimal = BigDecimal.ZERO,
    val settingsIconVisible:Boolean = true
)

