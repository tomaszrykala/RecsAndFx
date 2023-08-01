package com.tomaszrykala.recsandfx.core.domain.effect

import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.datatype.NativeEffect
import com.tomaszrykala.recsandfx.core.datatype.ParamDescription
import com.tomaszrykala.recsandfx.core.domain.R
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterface

data class Effect(
    val name: String,
    val category: String = "",
    val id: Int = 0,
    val icon: Int,
    val info: String = "This is a $name effect.",
    val params: List<Param> = emptyList(),
)

data class Param(
    val name: String,
    val minValue: Float,
    val maxValue: Float,
    val defaultValue: Float
)

fun ParamDescription.toParam() = Param(
    name = paramName,
    minValue = minValue,
    maxValue = maxValue,
    defaultValue = defaultValue
)

fun Param.toParamDescription() = ParamDescription(
    paramName = name,
    minValue = minValue,
    maxValue = maxValue,
    defaultValue = defaultValue
)

fun Effect.toNativeEffect() = NativeEffect(
    EffectDescription(
        name = name,
        category = category,
        id = id,
        paramValues = params.map { it.toParamDescription() }.toTypedArray()
    )
)

// DEBUG
//val oboeEffects = listOf(
//    Effect("Comb Filter", icon = Icons.Default.Add),
//    Effect("Delay Line", icon = Icons.Default.AccountBox),
//    Effect("Doubling", icon = Icons.Default.Build),
//    Effect("Drive Control", icon = Icons.Default.Email),
//    Effect("Echo", icon = Icons.Default.Favorite),
//    Effect("Effects", icon = Icons.Default.Info),
//    Effect("Flanger", icon = Icons.Default.Home),
//    Effect("Single Function", icon = Icons.Default.List),
//    Effect("Slapback", icon = Icons.Default.MoreVert),
//    Effect("Tremolo", icon = Icons.Default.Person),
//    Effect("Vibrato", icon = Icons.Default.Send),
//    Effect("White Chorus", icon = Icons.Default.ThumbUp),
//)

val allEffects: List<Effect> = NativeInterface.effectDescriptionMap.map {
    Effect(
        name = it.key,
        category = it.value.category,
        id = it.value.id,
        icon = R.drawable.ic_round_audiotrack_24,
        params = it.value.paramValues.map { pd -> pd.toParam() }
    )
}
