package com.coderzf1.grocerycalculator.models

import androidx.compose.ui.graphics.Color

data class KeypadButton(
    val id:Int,
    val text:String,
    val buttonColor: Color? = null
)