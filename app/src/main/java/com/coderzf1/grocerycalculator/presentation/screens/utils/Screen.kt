package com.coderzf1.grocerycalculator.presentation.screens.utils

sealed class Screen(val route:String){
    data object MainScreen:Screen("main")
    data object SettingsScreen:Screen("settings")
}