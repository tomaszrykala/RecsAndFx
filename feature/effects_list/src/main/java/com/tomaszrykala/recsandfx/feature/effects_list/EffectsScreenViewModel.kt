package com.tomaszrykala.recsandfx.feature.effects_list

import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EffectsScreenViewModel(
    private val effectsRepository: EffectsRepository
) : ViewModel() {

    private val stateFlow = MutableStateFlow<EffectsScreenState>(EffectsScreenState.Empty)
    val uiStateFlow: StateFlow<EffectsScreenState> = stateFlow

    suspend fun getEffects() {
        stateFlow.emit(EffectsScreenState.Effects(effectsRepository.getAllEffects()))
    }
}

sealed class EffectsScreenState {
    object Empty : EffectsScreenState()
    data class Effects(val effects: List<Effect>) : EffectsScreenState()
}