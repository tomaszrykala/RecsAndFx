package com.tomaszrykala.recsandfx.feature.effects_list

import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.EffectCategory
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class EffectsScreenViewModel(
    private val effectsRepository: EffectsRepository,
    private val dispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val stateFlow = MutableStateFlow<EffectsScreenUiState>(EffectsScreenUiState.Empty)
    val uiStateFlow: StateFlow<EffectsScreenUiState> = stateFlow

    suspend fun observeEffects() {
        val effects: Map<EffectCategory, List<Effect>> = withContext(dispatcher) {
            effectsRepository.getAllEffects().groupBy(
                keySelector = { it.category },
                valueTransform = { it },
            )
        }
        stateFlow.emit(EffectsScreenUiState.Effects(effects))
    }
}