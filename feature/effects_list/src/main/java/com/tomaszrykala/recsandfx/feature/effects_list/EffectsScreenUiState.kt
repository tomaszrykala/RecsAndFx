package com.tomaszrykala.recsandfx.feature.effects_list

import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.EffectCategory

sealed class EffectsScreenUiState {
    object Empty : EffectsScreenUiState()
    data class Effects(val effects: Map<EffectCategory, List<Effect>>) : EffectsScreenUiState()
}