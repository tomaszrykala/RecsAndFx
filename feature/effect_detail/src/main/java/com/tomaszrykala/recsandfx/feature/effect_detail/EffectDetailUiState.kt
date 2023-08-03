package com.tomaszrykala.recsandfx.feature.effect_detail

import com.tomaszrykala.recsandfx.core.domain.effect.Effect

sealed class EffectDetailUiState {
    object Empty : EffectDetailUiState()
    object Error : EffectDetailUiState()
    data class EffectDetail(val effect: Effect, val recordings: List<String>) : EffectDetailUiState()
}