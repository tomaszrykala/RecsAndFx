package com.tomaszrykala.recsandfx.feature.effect_detail

import androidx.annotation.StringRes
import com.tomaszrykala.recsandfx.core.domain.effect.Effect

sealed class EffectDetailUiState {
    object Empty : EffectDetailUiState()
    data class Error(@StringRes val errorResId: Int) : EffectDetailUiState()
    data class EffectDetail(val effect: Effect, val recordings: List<String>) : EffectDetailUiState()
}