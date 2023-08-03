package com.tomaszrykala.recsandfx.feature.effects_list

import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.Category
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class EffectsScreenViewModel(
    private val effectsRepository: EffectsRepository,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private val stateFlow = MutableStateFlow<EffectsScreenState>(EffectsScreenState.Empty)
    val uiStateFlow: StateFlow<EffectsScreenState> = stateFlow

    suspend fun observeEffects() {
        val effects: Map<Category, List<Effect>> = withContext(defaultDispatcher) {
            effectsRepository.getAllEffects().groupBy(
                keySelector = { it.category },
                valueTransform = { it },
            )
        }
        stateFlow.emit(EffectsScreenState.Effects(effects))
    }
}

sealed class EffectsScreenState {
    object Empty : EffectsScreenState()
    data class Effects(val effects: Map<Category, List<Effect>>) : EffectsScreenState()
}