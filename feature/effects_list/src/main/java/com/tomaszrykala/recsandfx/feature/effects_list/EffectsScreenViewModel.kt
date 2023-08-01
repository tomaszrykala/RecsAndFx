package com.tomaszrykala.recsandfx.feature.effects_list

import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.toParam
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapperImpl

class EffectsScreenViewModel(
    private val nativeInterface: NativeInterfaceWrapper = NativeInterfaceWrapperImpl(),
) : ViewModel() {

    private val allEffects: List<Effect> by lazy { // TODO EffectRepository
        nativeInterface.getAllEffectsMap().map {
            Effect(
                name = it.key,
                category = it.value.category,
                id = it.value.id,
                params = it.value.paramValues.map { pd -> pd.toParam() }
            )
        }
    }

    fun getEffects(): List<Effect> = allEffects // TODO FLOW
}