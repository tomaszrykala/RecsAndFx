package com.tomaszrykala.recsandfx.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.ui.graphics.vector.ImageVector

data class Effect(
    val name: String,
    val icon: ImageVector,
    val info: String = "This is a $name effect."
)

// DEBUG
val oboeEffects = listOf(
//    Effect("Delay", Icons.Default.Favorite),
//    Effect("Reverb", Icons.Default.Check),
//    Effect("Dynamics", Icons.Default.Refresh),
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