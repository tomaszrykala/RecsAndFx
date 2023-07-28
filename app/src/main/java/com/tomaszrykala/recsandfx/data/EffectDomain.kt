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

data class EffectDomain(
    val name: String,
    val icon: ImageVector,
    val info: String = "This is a $name effect."
)

// DEBUG
val oboeEffects = listOf(
    EffectDomain("Comb Filter", Icons.Default.Add),
    EffectDomain("Delay Line", Icons.Default.AccountBox),
    EffectDomain("Doubling", Icons.Default.Build),
    EffectDomain("Drive Control", Icons.Default.Email),
    EffectDomain("Echo", Icons.Default.Favorite),
    EffectDomain("Effects", Icons.Default.Info),
    EffectDomain("Flanger", Icons.Default.Home),
    EffectDomain("Single Function", Icons.Default.List),
    EffectDomain("Slapback", Icons.Default.MoreVert),
    EffectDomain("Tremolo", Icons.Default.Person),
    EffectDomain("Vibrato", Icons.Default.Send),
    EffectDomain("White Chorus", Icons.Default.ThumbUp),
)

val juceEffects = listOf(
    EffectDomain("Delay", Icons.Default.Favorite),
    EffectDomain("Reverb", Icons.Default.Check),
    EffectDomain("Dynamics", Icons.Default.Refresh),
)