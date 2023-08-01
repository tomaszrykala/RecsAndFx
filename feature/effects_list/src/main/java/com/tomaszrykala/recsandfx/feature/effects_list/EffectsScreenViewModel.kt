package com.tomaszrykala.recsandfx.feature.effects_list

import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository

class EffectsScreenViewModel(
    private val effectsRepository: EffectsRepository
) : ViewModel() {

    // TODO FLOW
    fun getEffects(): List<Effect> = effectsRepository.getAllEffects()
}