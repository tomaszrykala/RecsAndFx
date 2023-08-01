package com.tomaszrykala.recsandfx.core.domain.effect

import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.datatype.NativeEffect

data class Effect(
    val name: String,
    val category: String = "",
    val id: Int = 0,
    val icon: Int,
    val info: String = "This is a $name effect.",
    val params: List<Param> = emptyList(),
)

fun Effect.toNativeEffect() = NativeEffect(
    EffectDescription(
        name = name,
        category = category,
        id = id,
        paramValues = params.map { it.toParamDescription() }.toTypedArray()
    )
)