package com.coderzf1.grocerycalculator.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.coderzf1.grocerycalculator.presentation.screens.ActionId
import com.coderzf1.grocerycalculator.presentation.screens.MainScreen
import com.coderzf1.grocerycalculator.presentation.screens.SettingsScreen
import com.coderzf1.grocerycalculator.presentation.screens.components.MainActivityAppBar
import com.coderzf1.grocerycalculator.presentation.screens.utils.Screen
import com.coderzf1.grocerycalculator.presentation.ui.theme.GroceryCalculatorTheme


class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GroceryCalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val viewModel: MainActivityViewModel by viewModels()
                    val state by viewModel.mainActivityState.collectAsState()
                    val foodStampSwitchChecked = state.isFoodStamp
                    val navController = rememberNavController()
                    Scaffold (
                        topBar = {
                            MainActivityAppBar(
                                foodStampSwitchChecked = foodStampSwitchChecked,
                                foodStampSwitchOnCheckedChanged = {
                                    viewModel.updateFoodStampSwitchState(it)
                                },
                                settingsClicked = {
                                    navController.navigate(Screen.SettingsScreen.route)
                                }
                            )
                        }
                    ){
                        Box (modifier = Modifier.fillMaxSize().padding(it)) {
                            NavHost(navController = navController, Screen.MainScreen.route){
                                composable (route = Screen.MainScreen.route){
                                    MainScreen(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        state = state,
                                        buttonClicked = {actionId, s ->
                                            when(actionId){
                                                ActionId.DELETE_LAST_CHARACTER -> viewModel.deleteLastCharacter()
                                                ActionId.ADD_ENTRY_FOOD -> viewModel.addEntry(EntryType.FOOD)
                                                ActionId.ADD_ENTRY_NONFOOD -> viewModel.addEntry(EntryType.NONFOOD)
                                                ActionId.SET_QUANTITY -> viewModel.setQty()
                                                ActionId.CLEAR_ALL -> viewModel.clearAll()
                                                ActionId.APPEND_CHARACTER -> viewModel.appendCharacterToEntry(s!!)
                                            }
                                        },
                                        itemDeleted = {moneyEntry ->
                                            viewModel.deleteEntry(moneyEntry!!)
                                        }
                                    )
                                }
                                composable (route = Screen.SettingsScreen.route){
                                    SettingsScreen(
                                        modifier = Modifier.fillMaxSize(),
                                        state = state,
                                        saveSettingsClicked = { foodTax, nonFoodTax ->
                                            viewModel.setFoodTax(foodTax)
                                            viewModel.setNonFoodTax(nonFoodTax)
                                            navController.popBackStack()
                                        },
                                        backArrowClicked = {
                                            navController.popBackStack()
                                        }
                                    )
                                }
                            }
                        }


                    }
                }
            }
        }
    }
}


