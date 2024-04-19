package com.coderzf1.grocerycalculator.presentation.screens.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.coderzf1.grocerycalculator.R
import com.coderzf1.grocerycalculator.presentation.utils.withSound

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainActivityAppBar(
    foodStampSwitchChecked:Boolean,
    foodStampSwitchOnCheckedChanged:(Boolean) -> Unit,
    settingsClicked:() -> Unit
){
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Grocery Calculator",
                    modifier = Modifier.weight(1f),
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontFamily = FontFamily(
                            Font(
                            R.font.montserrat
                            )
                        )
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Food Stamps",
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontFamily = FontFamily(
                            Font(
                                R.font.montserrat
                            )
                        ),
                        fontSize = TextUnit(10f, TextUnitType.Sp)
                    )
                )
                Spacer(modifier = Modifier.width(4.dp))
                val switchColors =
                    if(isSystemInDarkTheme()) {
                        SwitchDefaults.colors().copy(
                            checkedThumbColor = MaterialTheme.colorScheme.onPrimaryContainer,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            checkedBorderColor = Color.Transparent,
                            checkedIconColor = MaterialTheme.colorScheme.primary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.background,
                            uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedBorderColor = Color.Transparent,
                            uncheckedIconColor = Color.Transparent
                        )
                    } else {
                        SwitchDefaults.colors().copy(
                            checkedThumbColor = MaterialTheme.colorScheme.tertiary,
                            checkedTrackColor = MaterialTheme.colorScheme.primaryContainer,
                            checkedBorderColor = Color.Transparent,
                            checkedIconColor = MaterialTheme.colorScheme.onTertiary,
                            uncheckedThumbColor = MaterialTheme.colorScheme.background,
                            uncheckedTrackColor = MaterialTheme.colorScheme.tertiary,
                            uncheckedBorderColor = Color.Transparent,
                            uncheckedIconColor = Color.Transparent
                        )
                    }

                Switch(checked = foodStampSwitchChecked,
                    onCheckedChange = {
                        foodStampSwitchOnCheckedChanged(it)
                    },
                    colors = switchColors,
                    thumbContent = {
                        Box(modifier = Modifier.padding(4.dp)){
                            Icon(
                                imageVector = Icons.Filled.Check,
                                contentDescription = null
                            )
                        }

                    }
                )
                Spacer(modifier = Modifier.width(4.dp))
                IconButton(
                    onClick = {
                        settingsClicked()
                    }.withSound(LocalContext.current)
                ) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(4.dp))
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}