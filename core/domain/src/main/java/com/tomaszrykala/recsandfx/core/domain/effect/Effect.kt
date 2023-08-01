package com.tomaszrykala.recsandfx.core.domain.effect

import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.datatype.NativeEffect
import com.tomaszrykala.recsandfx.core.domain.R

data class Effect(
    val id: Int,
    val name: String,
    val category: String,
    val params: List<Param>,
    val icon: Int = R.drawable.ic_round_audiotrack_24,
    val info: String = "Effect of '$category' category."
)

fun Effect.toNativeEffect() = NativeEffect(
    EffectDescription(
        id = id,
        name = name,
        category = category,
        paramValues = params.map { it.toParamDescription() }.toTypedArray()
    )
)