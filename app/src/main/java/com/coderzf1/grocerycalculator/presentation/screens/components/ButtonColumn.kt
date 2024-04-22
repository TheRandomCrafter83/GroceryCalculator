package com.coderzf1.grocerycalculator.presentation.screens.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.coderzf1.grocerycalculator.models.KeypadButton

@Composable
fun ButtonColumn(
    modifier: Modifier = Modifier,
    buttonClicked: (button: KeypadButton) -> Unit,
    rows: List<List<KeypadButton>> = emptyList()
){
    Column(
        modifier = modifier
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        rows.forEach { rowInfo ->
            ButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                keypadButtons = rowInfo
            ) {
                buttonClicked(it)
            }
        }
    }
}