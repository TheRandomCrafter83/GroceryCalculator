package com.coderzf1.grocerycalculator.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.coderzf1.grocerycalculator.R
import com.coderzf1.grocerycalculator.models.KeypadButton
import com.coderzf1.grocerycalculator.presentation.utils.withSound

@Composable
fun ButtonRow(
    modifier: Modifier = Modifier,
    keypadButtons:List<KeypadButton>,
    buttonClicked:(button: KeypadButton)-> Unit
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        keypadButtons.forEach { button ->
            Button(
                onClick = {
                    buttonClicked(button)
                }.withSound(LocalContext.current), modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = button.buttonColor ?: MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(8.dp),
                contentPadding = PaddingValues(4.dp)
            ) {
                Text(
                    button.text, style = MaterialTheme.typography.titleMedium.copy(
                        fontFamily = FontFamily(
                            Font(R.font.montserrat)
                        ),
                        fontSize = TextUnit(14f, TextUnitType.Sp)
                    ),
                    textAlign = TextAlign.Center,
                    lineHeight = TextUnit(14f,TextUnitType.Sp)
                )
            }
        }
    }
}