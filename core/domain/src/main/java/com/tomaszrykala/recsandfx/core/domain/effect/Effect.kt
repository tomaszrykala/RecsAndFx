package com.tomaszrykala.recsandfx.core.domain.effect

import androidx.annotation.StringRes
import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.datatype.NativeEffect

data class Effect(
    val id: Int,
    val name: String,
    val params: List<Param>,
    val category: EffectCategory,
    @StringRes val description: Int,
)

fun Effect.toNativeEffect() = NativeEffect(
    EffectDescription(
        id = id,
        name = name,
        category = category.name,
        paramValues = params.map { it.toParamDescription() }.toTypedArray()
    )
)