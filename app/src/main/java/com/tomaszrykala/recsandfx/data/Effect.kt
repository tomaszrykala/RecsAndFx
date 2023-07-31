package com.tomaszrykala.recsandfx.data

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
import com.tomaszrykala.recsandfx.data.datatype.ParamDescription

data class Effect(
    val name: String,
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

// DEBUG
val oboeEffects = listOf(
    Effect("Comb Filter", Icons.Default.Add),
    Effect("Delay Line", Icons.Default.AccountBox),
    Effect("Doubling", Icons.Default.Build),
    Effect("Drive Control", Icons.Default.Email),
    Effect("Echo", Icons.Default.Favorite),
    Effect("Effects", Icons.Default.Info),
    Effect("Flanger", Icons.Default.Home),
    Effect("Single Function", Icons.Default.List),
    Effect("Slapback", Icons.Default.MoreVert),
    Effect("Tremolo", Icons.Default.Person),
    Effect("Vibrato", Icons.Default.Send),
    Effect("White Chorus", Icons.Default.ThumbUp),
)

val juceEffects = listOf(
    Effect("Delay", Icons.Default.Favorite),
    Effect("Reverb", Icons.Default.Check),
    Effect("Dynamics", Icons.Default.Refresh),
)

val oboeRealFx: List<Effect> = NativeInterface.effectDescriptionMap.map {
    Effect(
        it.key, Icons.Default.Add, params = it.value.paramValues.map { pd -> pd.toParam() }
    )
}
