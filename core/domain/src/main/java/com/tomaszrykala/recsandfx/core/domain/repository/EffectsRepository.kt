package com.tomaszrykala.recsandfx.core.domain.repository

import com.tomaszrykala.recsandfx.core.domain.R
import com.tomaszrykala.recsandfx.core.domain.effect.EffectCategory
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.toParam
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper

interface EffectsRepository {
    suspend fun getAllEffects(): List<Effect>
}

internal class EffectsRepositoryImpl(
    private val nativeInterface: NativeInterfaceWrapper,
) : EffectsRepository {

    private val effects: List<Effect> by lazy {
        nativeInterface.getAllEffectsMap().map {
            Effect(
                id = it.value.id,
                name = it.key,
                params = it.value.paramValues.map { pd -> pd.toParam() },
                category = it.value.category.toCategory(),
                shortDescription = mapShortDescription(it.key),
                icon = R.drawable.ic_round_audiotrack_24,
            )
        }
    }

    // Add strings to Resources. Not liking mapping by String name tbh.
    private fun mapShortDescription(name: String): String {
        return when (name) {
            "Echo" -> "Simple time-delayed, repeated version of the input, simulating echoes."
            "Doubling" -> "A more advanced and thicker-sounding Echo effect."
            "Slapback" -> "Short, single echo with minimal delay, adding a sense of space and depth."
            "Flanger" -> "A classic, sweeping sound modulation, by mixing the input with a phased copy."
            "Vibrato" -> "Warbling or wobbling effect that simulates a fluctuation in pitch."
            "White Chorus" -> "A rich, shimmering sound from multiple modulated copies of the input."
            "Overdrive" -> "Warm, saturated sound of overdriven tube amplifiers rich with overtones."
            "Distortion" -> "Harsh, gritty and distorted sound produced by clipping or saturation."
            "AllPass" -> "Series of delayed and phase-shifted copies, producing a comb-like frequency response."
            "FIR" -> "Comb-like frequency response with adjustable feedback and feedforward coefficients."
            "IIR" -> "Similar to FIR, with added Scale modulation."
            "Gain" -> "Adjusts the amplitude or volume level of the audio signal."
            "Tremolo" -> "Rhythmic modulation of the input amplitude, creating a pulsating or trembling sound."
            "Passthrough" -> "No modulation. Just recording."
            else -> ""
        }
    }

    override suspend fun getAllEffects(): List<Effect> = effects
}

private fun String.toCategory(): EffectCategory =
    EffectCategory.values().find { it.name == this } ?: EffectCategory.None