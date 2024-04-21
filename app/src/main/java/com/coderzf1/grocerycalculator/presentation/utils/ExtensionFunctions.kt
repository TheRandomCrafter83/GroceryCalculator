package com.coderzf1.grocerycalculator.presentation.utils

import android.content.Context
import android.media.AudioManager
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.NumberFormat

fun (() -> Unit).withSound(context: Context): () -> Unit = {
    (context.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        .playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
    this()
}

fun BigDecimal.toCurrencyFormat():String {
    val formatter = NumberFormat.getCurrencyInstance()
    formatter.roundingMode = RoundingMode.HALF_UP
    return formatter.format(this)
}
