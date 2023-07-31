package com.tomaszrykala.recsandfx.core

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector
import com.tomaszrykala.recsandfx.core.datatype.EffectDescription
import com.tomaszrykala.recsandfx.core.datatype.NativeEffect
import com.tomaszrykala.recsandfx.core.datatype.ParamDescription
import com.tomaszrykala.recsandfx.core.native.NativeInterface

data class Effect(
    val name: String,
    val category: String = "",
    val id: Int = 0,
    val icon: ImageVector,
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
val oboeEffects = listOf(
    Effect("Comb Filter", icon = Icons.Default.Add),
    Effect("Delay Line", icon = Icons.Default.AccountBox),
    Effect("Doubling", icon = Icons.Default.Build),
    Effect("Drive Control", icon = Icons.Default.Email),
    Effect("Echo", icon = Icons.Default.Favorite),
    Effect("Effects", icon = Icons.Default.Info),
    Effect("Flanger", icon = Icons.Default.Home),
    Effect("Single Function", icon = Icons.Default.List),
    Effect("Slapback", icon = Icons.Default.MoreVert),
    Effect("Tremolo", icon = Icons.Default.Person),
    Effect("Vibrato", icon = Icons.Default.Send),
    Effect("White Chorus", icon = Icons.Default.ThumbUp),
)

val juceEffects = listOf(
    Effect("Delay", icon = Icons.Default.Favorite),
    Effect("Reverb", icon = Icons.Default.Check),
    Effect("Dynamics", icon = Icons.Default.Refresh),
)

val allEffects: List<Effect> = NativeInterface.effectDescriptionMap.map {
    Effect(
        name = it.key,
        category = it.value.category,
        id = it.value.id,
        icon = Icons.Default.Add,
        params = it.value.paramValues.map { pd -> pd.toParam() }
    )
}
