package com.tomaszrykala.recsandfx.core.domain.repository

import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.effect.toParam
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper

interface EffectsRepository {
    suspend fun getAllEffects(): List<Effect>
}

internal class EffectsRepositoryImpl(
    private val nativeInterface: NativeInterfaceWrapper,
) : EffectsRepository {

    private val effects: List<Effect> by lazy {
        nativeInterface.getAllEffectsMap().map {
            Effect(
                id = it.value.id,
                name = it.key,
                category = it.value.category,
                params = it.value.paramValues.map { pd -> pd.toParam() },
            )
        }
    }

    override suspend fun getAllEffects(): List<Effect> = effects
}