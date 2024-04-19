package com.coderzf1.grocerycalculator.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.coderzf1.grocerycalculator.R
import com.coderzf1.grocerycalculator.models.KeypadButton
import com.coderzf1.grocerycalculator.models.MoneyEntry
import com.coderzf1.grocerycalculator.presentation.screens.components.ButtonColumn
import com.coderzf1.grocerycalculator.presentation.states.MainScreenState
import com.coderzf1.grocerycalculator.presentation.ui.theme.colorDarkHighlight
import com.coderzf1.grocerycalculator.presentation.ui.theme.colorHighlight
import com.coderzf1.grocerycalculator.presentation.utils.toCurrencyFormat
import java.math.BigDecimal

private val hundred: BigDecimal = BigDecimal(100)

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    state: MainScreenState = MainScreenState(),
    buttonClicked: (ActionId, String?) -> Unit,
    itemDeleted:(MoneyEntry?) -> Unit?
) {
    Column(
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiary),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,

                ) {
                val number = if (state.entry.isNotEmpty()) {
                    BigDecimal(state.entry.joinToString(""))
                } else {
                    BigDecimal(0)
                }
                val textToDisplay = number.divide(hundred).toCurrencyFormat() //Utils.formatCurrency(number.divide(hundred))

                Text(
                    text = "Qty: ${state.quantity} $textToDisplay",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp, fontFamily = FontFamily(
                            Font(
                                R.font.montserrat
                            )
                        )
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .weight(.6f)
                .background(MaterialTheme.colorScheme.background),
            state = rememberLazyListState()
        ) {
            items(
                items = state.entries.toList(),
                key = { entry ->
                    entry.id
                }
            ) { value ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (state.entries.indexOf(value) % 2 == 0) {
                                MaterialTheme.colorScheme.onPrimary.copy(alpha = .75f)
                            } else {
                                MaterialTheme.colorScheme.background
                            }
                        )
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Qty: ${value.qty} - ${
                                value.amount.divide(hundred).toCurrencyFormat()
                            }",
                            modifier = Modifier.weight(1f),
                            color = if (state.entries.indexOf(value) % 2 == 0) {
                                MaterialTheme.colorScheme.primary.copy(alpha = .75f)
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(
                            text = value.entryType.name.first().uppercaseChar()
                                .toString(),
                            color = if (state.entries.indexOf(value) % 2 == 0) {
                                MaterialTheme.colorScheme.primary.copy(alpha = .75f)
                            } else {
                                MaterialTheme.colorScheme.onBackground
                            }
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        IconButton(
                            onClick = {
                                itemDeleted(value)
                            }) {
                            Icon(
                                imageVector = Icons.Filled.Delete,
                                contentDescription = "Delete Item ${value.amount}",
                                tint =
                                if(state.entries.indexOf(value) % 2 == 0) {
                                    Color(75, 0, 0, 0xFF)
                                } else {
                                    if(isSystemInDarkTheme()) {
                                        Color(0x9E, 0x64, 0x64, 0xFF)
                                    } else {
                                        Color(75, 0, 0, 0xFF)
                                    }
                                }
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                }

            }
        }
        ButtonColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .background(if (isSystemInDarkTheme()) colorDarkHighlight else colorHighlight),
            buttonClicked = { button ->
                when (button.id) {
                    3 -> buttonClicked(ActionId.DELETE_LAST_CHARACTER, null)
                    7 -> buttonClicked(ActionId.ADD_ENTRY_FOOD, null)
                    11 -> buttonClicked(ActionId.ADD_ENTRY_NONFOOD, null)
                    14 -> buttonClicked(ActionId.SET_QUANTITY, null)
                    15 -> buttonClicked(ActionId.CLEAR_ALL, null)
                    else -> buttonClicked(ActionId.APPEND_CHARACTER, button.text)
                }
            },
            rows = listOf(
                listOf(
                    KeypadButton(0, "7"),
                    KeypadButton(1, "8"),
                    KeypadButton(2, "9"),
                    KeypadButton(
                        3, "<<"
                    )
                ),
                listOf(
                    KeypadButton(4, "4"),
                    KeypadButton(5, "5"),
                    KeypadButton(6, "6"),
                    KeypadButton(
                        7, "Add\nFood"
                    )
                ),
                listOf(
                    KeypadButton(8, "1"),
                    KeypadButton(9, "2"),
                    KeypadButton(10, "3"),
                    KeypadButton(
                        11, "Add\nNonfood"
                    )
                ),
                listOf(
                    KeypadButton(12, "0"),
                    KeypadButton(13, "00"),
                    KeypadButton(14, "QTY"),
                    KeypadButton(15, "C", buttonColor = Color(75, 0, 0, 0xFF))
                )
            )
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,

                ) {
                Text(
                    text = "Subtotal: ${state.subtotal.divide(hundred).toCurrencyFormat()}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp, fontFamily = FontFamily(
                            Font(
                                R.font.montserrat
                            )
                        )
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,

                ) {
                Text(
                    text = "Tax ${state.totalTax.divide(hundred).toCurrencyFormat()}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp, fontFamily = FontFamily(
                            Font(
                                R.font.montserrat
                            )
                        )
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.End,

                ) {
                Text(
                    text = "Total ${state.total.divide(hundred).toCurrencyFormat()}",
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp, fontFamily = FontFamily(
                            Font(
                                R.font.montserrat
                            )
                        )
                    ),
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

enum class ActionId {
    DELETE_LAST_CHARACTER,
    ADD_ENTRY_FOOD,
    ADD_ENTRY_NONFOOD,
    SET_QUANTITY,
    CLEAR_ALL,
    APPEND_CHARACTER
}