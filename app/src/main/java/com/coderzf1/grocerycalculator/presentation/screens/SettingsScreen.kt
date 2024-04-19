package com.coderzf1.grocerycalculator.presentation.screens

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.coderzf1.grocerycalculator.R
import com.coderzf1.grocerycalculator.presentation.states.MainScreenState
import com.coderzf1.grocerycalculator.presentation.utils.withSound
import java.math.BigDecimal

@Composable
fun SettingsScreen(
    modifier:Modifier = Modifier,
    state: MainScreenState,
    saveSettingsClicked:(foodTax:BigDecimal,nonFoodTax:BigDecimal) -> Unit,
    backArrowClicked:() -> Unit
){
    var foodTax by remember{mutableStateOf(state.foodTax.multiply(BigDecimal(100)).toInt().toString())}
    var nonFoodTax by remember{mutableStateOf(state.nonfoodTax.multiply(BigDecimal(100)).toInt().toString())}
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                onClick = { backArrowClicked()
                }.withSound(LocalContext.current)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack,"Back", tint = if(isSystemInDarkTheme())MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary)
            }
            Text(
                text = "Settings",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Start,
                color = if(isSystemInDarkTheme())MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary
            )
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
        ){
            Column {
                val focusColor = if(isSystemInDarkTheme()){
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.primary
                }

                val fieldColors = TextFieldDefaults.colors().copy(
                    focusedLabelColor = focusColor,
                    focusedIndicatorColor = focusColor
                )

                OutlinedTextField(
                    value = foodTax,
                    onValueChange = {
                        foodTax = it
                    },
                    placeholder = {
                        Text("Food Tax %:")
                    },
                    label = {
                        Text("Food Tax %:")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = VisualTransformation.None,
                    trailingIcon = {
                        Icon(painterResource(id = R.drawable.ic_percent),null, tint = (if(isSystemInDarkTheme())MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary).copy(alpha = .5f))
                    },
                    isError = foodTax.isEmpty(),
                    supportingText = {
                        if(foodTax.isEmpty()){
                            Text("Value cannot be empty.")
                        }
                    },
                    singleLine = true,
                    colors = fieldColors
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = nonFoodTax,
                    onValueChange = {
                        nonFoodTax = it
                    },
                    placeholder = {
                        Text("Nonfood Tax %:")

                    },
                    label = {
                        Text("Nonfood Tax %:")
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    visualTransformation = VisualTransformation.None,
                    trailingIcon = {
                        Icon(painterResource(id = R.drawable.ic_percent),null, tint = (if(isSystemInDarkTheme())MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.primary).copy(alpha = .5f))
                    },
                    isError = nonFoodTax.isEmpty(),
                    supportingText = {
                        if(nonFoodTax.isEmpty()){
                            Text("Value cannot be empty.")
                        }
                    },
                    singleLine = true,
                    colors = fieldColors
                )
            }
        }

        Button(
            onClick = {
                if(foodTax.isNotEmpty() and nonFoodTax.isNotEmpty()) {
                    saveSettingsClicked(
                        BigDecimal(foodTax).divide(BigDecimal(100)),
                        BigDecimal(nonFoodTax).divide(BigDecimal(100))
                    )
                }

            }.withSound(LocalContext.current),
            contentPadding = PaddingValues(16.dp,8.dp),
            colors = ButtonDefaults.buttonColors().copy(
                containerColor = if(isSystemInDarkTheme()){
                    MaterialTheme.colorScheme.secondary
                } else {
                    MaterialTheme.colorScheme.primary
                }
            )
        ) {
            Text("Save Settings")
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}