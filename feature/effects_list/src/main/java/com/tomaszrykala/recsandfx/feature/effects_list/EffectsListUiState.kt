package com.tomaszrykala.recsandfx.feature.effects_list

import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.EffectCategory

sealed class EffectsListUiState {
    object Empty : EffectsListUiState()
    data class EffectsList(val effects: Map<EffectCategory, List<Effect>>) : EffectsListUiState()
}