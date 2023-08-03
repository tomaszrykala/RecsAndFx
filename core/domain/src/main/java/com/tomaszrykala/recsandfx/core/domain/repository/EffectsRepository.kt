package com.tomaszrykala.recsandfx.core.domain.repository

import androidx.annotation.StringRes
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

    override suspend fun getAllEffects(): List<Effect> = effects

    private val effects: List<Effect> by lazy {
        nativeInterface.getAllEffectsMap().map {
            Effect(
                id = it.value.id,
                name = it.key,
                params = it.value.paramValues.map { pd -> pd.toParam() },
                category = it.value.category.toCategory(),
                description = getDescription(it.key)
            )
        }
    }

    @StringRes
    private fun getDescription(name: String): Int {
        return when (name) {
            "Echo" -> R.string.effect_desc_echo
            "Doubling" -> R.string.effect_desc_doubling
            "Slapback" -> R.string.effect_desc_slapback
            "Flanger" -> R.string.effect_desc_flanger
            "Vibrato" -> R.string.effect_desc_vibrato
            "White Chorus" -> R.string.effect_desc_white_chorus
            "Overdrive" -> R.string.effect_desc_overdrive
            "Distortion" -> R.string.effect_desc_distortion
            "AllPass" -> R.string.effect_desc_allpass
            "FIR" -> R.string.effect_desc_fir
            "IIR" -> R.string.effect_desc_iir
            "Gain" -> R.string.effect_desc_gain
            "Tremolo" -> R.string.effect_desc_tremolo
            "Passthrough" -> R.string.effect_desc_passthrough
            else -> R.string.effect_desc_no_info
        }
    }
}

private fun String.toCategory(): EffectCategory =
    EffectCategory.values().find { it.name == this } ?: EffectCategory.None