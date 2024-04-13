package com.coderzf1.grocerycalculator.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.coderzf1.grocerycalculator.models.MoneyEntry
import com.coderzf1.grocerycalculator.presentation.states.MainScreenState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name="settings")

class MainActivityViewModel(app: Application) : AndroidViewModel(app) {
    var mainActivityState = MutableStateFlow(MainScreenState())
        private set

    private val nonFoodTaxSetting = stringPreferencesKey("nonfood_tax")
    private val foodTaxSetting    = stringPreferencesKey("food_tax")

    init {
        viewModelScope.launch {
            val ft = BigDecimal(getFoodTaxSetting(app))
            val nt = BigDecimal(getNonFoodTaxSetting(app))
            Log.d("INIT", "foodTax: $ft - nonfoodTax: $nt")
            mainActivityState.update {state ->
                state.copy(
                    foodTax =  ft,
                    nonfoodTax = nt
                )
            }
        }
    }

    fun updateFoodStampSwitchState(value:Boolean) {
        mainActivityState.update {
            it.copy(isFoodStamp = value)
        }
        calculate()
    }

    private suspend fun getFoodTaxSetting(context: Context):String{
        return context.dataStore.data.firstOrNull()?.get(foodTaxSetting)?:".03"
    }

    private suspend fun getNonFoodTaxSetting(context:Context):String{
        return context.dataStore.data.firstOrNull()?.get(nonFoodTaxSetting)?:".07"
    }

    private suspend fun saveFoodTaxSetting(context:Context){
        context.dataStore.edit {preferences ->
            preferences[foodTaxSetting] = mainActivityState.value.foodTax.toString()
        }
    }

    private suspend fun saveNonFoodTaxSetting(context:Context){
        context.dataStore.edit {preferences ->
            preferences[nonFoodTaxSetting] = mainActivityState.value.nonfoodTax.toString()
        }
    }

    fun setFoodTax(foodTax:BigDecimal){
        mainActivityState.update { state ->
            state.copy(foodTax = foodTax)
        }
        updateSettings(getApplication())
        calculate()
    }

    fun setNonFoodTax(nonFoodTax:BigDecimal){
        mainActivityState.update { state ->
            state.copy(nonfoodTax = nonFoodTax)
        }
        updateSettings(getApplication())
        calculate()
    }



    private fun updateSettings(context: Context){
        viewModelScope.launch {
            saveFoodTaxSetting(context)
            saveNonFoodTaxSetting(context)
        }
    }

    fun appendCharacterToEntry(character: String) {
        if (character == "00") {
            mainActivityState.update { state ->
                state.copy(
                    entry = state.entry + BigDecimal.ZERO + BigDecimal.ZERO
                )
            }
        } else {
            mainActivityState.update { state ->
                state.copy(
                    entry = state.entry + BigDecimal(character)
                )
            }
        }
    }

    fun deleteLastCharacter() {
        mainActivityState.update { state ->
            state.copy(
                entry = state.entry - state.entry.last()
            )
        }
    }

    private fun clearEntry() {
        mainActivityState.update { state ->
            state.copy(entry = emptyList())
        }
    }

    fun setQty() {
        mainActivityState.update { state ->
            state.copy(
                quantity = if(state.entry.isEmpty()) BigDecimal.ONE else BigDecimal(state.entry.joinToString ("")),
                entry = emptyList()
            )
        }
    }

    fun addEntry(entryType: EntryType) {
        mainActivityState.update { state ->
            state.copy(
                entries = state.entries +
                        MoneyEntry(
                            amount = BigDecimal(state.entry.joinToString("")),
                            qty = state.quantity,
                            entryType = entryType
                        )
            )
        }
        clearEntry()
        mainActivityState.update {state ->
            state.copy(quantity = BigDecimal.ONE)
        }
        calculate()
    }

    fun deleteEntry(entry:MoneyEntry) {
        mainActivityState.update { state ->
            state.copy(
                entries = state.entries - entry
            )
        }
        clearEntry()
        mainActivityState.update {state ->
            state.copy(quantity = BigDecimal.ONE)
        }
        calculate()
    }

    fun clearAll() {
        mainActivityState.update {
            MainScreenState()
        }
    }

    private fun clearSubTotal() {
        mainActivityState.update { state ->
            state.copy(subtotal = BigDecimal.ZERO)
        }
    }

    private fun clearTotal() {
        mainActivityState.update { state ->
            state.copy(total = BigDecimal.ZERO)
        }
    }

    private fun updateSubTotal(amount: BigDecimal) {
        mainActivityState.update { state ->
            state.copy(subtotal = state.subtotal.add(amount))
        }
    }

    private fun updateTotal(amount: BigDecimal) {
        mainActivityState.update { state ->
            state.copy(total = state.total.add(amount))
        }
    }

    private fun calculate() {
        clearSubTotal()
        clearTotal()
        viewModelScope.launch {
            val state = mainActivityState.value
            state.entries.forEach { moneyEntry ->
                updateSubTotal(moneyEntry.amount.multiply(moneyEntry.qty))
                val taxAmount: BigDecimal = when (moneyEntry.entryType) {
                    EntryType.FOOD -> {
                        if(state.isFoodStamp) BigDecimal.ZERO else state.foodTax
                    }

                    EntryType.NONFOOD -> {
                        state.nonfoodTax
                    }

                    EntryType.NONTAXED -> {
                        BigDecimal.ZERO
                    }
                }
                updateTotal(
                    moneyEntry.amount
                        .multiply(moneyEntry.qty)
                        .add(
                            moneyEntry.amount.multiply(taxAmount)
                        )
                )
            }
        }
    }
}

enum class EntryType {
    FOOD, NONFOOD, NONTAXED
}