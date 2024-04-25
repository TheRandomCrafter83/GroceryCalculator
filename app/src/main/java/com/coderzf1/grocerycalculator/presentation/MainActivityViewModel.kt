package com.coderzf1.grocerycalculator.presentation

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
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
    private val showTapTargetSetting     = booleanPreferencesKey("show_tap_target")

    private var showTapTargets:Boolean = true
    init {
        viewModelScope.launch {
            val ft = BigDecimal(getFoodTaxSetting(app))
            val nt = BigDecimal(getNonFoodTaxSetting(app))
            //val showTapTarget = getShowTapTargetSetting(app)
            Log.d("INIT", "foodTax: $ft - nonfoodTax: $nt - showTapTargets: $showTapTargets")
            mainActivityState.update {state ->
                state.copy(
                    foodTax =  ft,
                    nonfoodTax = nt,
                    showTapTarget = getShowTapTargetSetting(app)
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

    private suspend fun getShowTapTargetSetting(context:Context):Boolean{
        return context.dataStore.data.firstOrNull()?.get(showTapTargetSetting)?:true
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

    private suspend fun saveShowTapTargetSetting(context:Context){
        context.dataStore.edit {preferences ->
            preferences[showTapTargetSetting] = false
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

    fun setTapTargetDone(){
        viewModelScope.launch {
            saveShowTapTargetSetting(getApplication())
        }
        mainActivityState.update {state ->
            state.copy(showTapTarget = false)
        }
        Log.d("VM","Saved TapTarget")
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
            if(state.entry.isNotEmpty()) {
                state.copy(
                    entry = state.entry - state.entry.last()
                )
            } else {
                state.copy()
            }
        }
    }

    private fun clearEntry() {
        mainActivityState.update { state ->
            state.copy(entry = emptyList())
        }
    }

    fun setQty() {
        mainActivityState.update { state ->
            var entryAsBigDecimal = if (state.entry.isEmpty()) BigDecimal.ONE else BigDecimal(state.entry.joinToString(""))
            if (entryAsBigDecimal == BigDecimal.ZERO) entryAsBigDecimal = BigDecimal.ONE
            state.copy(
                quantity = entryAsBigDecimal,
                entry = emptyList()
            )

        }
    }

    fun addEntry(entryType: EntryType) {

        mainActivityState.update { state ->
            val entryAsBigDecimal = if(state.entry.isNotEmpty()){
                BigDecimal(state.entry.joinToString (""))
            } else {
                BigDecimal.ZERO
            }
            if(entryAsBigDecimal > BigDecimal.ZERO){
                state.copy(
                    entries = state.entries +
                            MoneyEntry(
                                amount = BigDecimal(state.entry.joinToString("")),
                                qty = state.quantity,
                                entryType = entryType
                            )
                )
            } else {
                state.copy()
            }
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
            MainScreenState(showTapTarget = false)
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

    private fun clearTax(){
        mainActivityState.update { state ->
            state.copy(totalTax = BigDecimal.ZERO)
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

    private fun updateTaxTotal(taxAmount:BigDecimal){
        mainActivityState.update {state ->
            state.copy(
                totalTax = state.totalTax.add(taxAmount)
            )
        }
    }

    fun updateSettingsIconVisible(visible:Boolean){
        mainActivityState.update {state ->
            state.copy(
                settingsIconVisible = visible
            )
        }
    }

    private fun calculateTax(){
        clearTax()
        viewModelScope.launch {
            val state = mainActivityState.value
            if(state.entries.isNotEmpty()) {
                state.entries.forEach { moneyEntry ->
                    val amount = moneyEntry.amount
                    val qty = moneyEntry.qty
                    val taxAmount = when (moneyEntry.entryType) {
                        EntryType.FOOD -> if (state.isFoodStamp) BigDecimal.ZERO else state.foodTax
                        EntryType.NONFOOD -> state.nonfoodTax
                        EntryType.NONTAXED -> BigDecimal.ZERO
                    }
                    updateTaxTotal(
                        amount.multiply(qty).multiply(taxAmount)
                    )
                }
            }
        }
    }

    private fun calculate() {
        clearSubTotal()
        clearTotal()
        calculateTax()
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