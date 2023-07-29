package com.tomaszrykala.recsandfx.ui.screen

sealed class Screen(val route: String) {
    object EffectsScreen : Screen("list")
    object EffectDetailScreen : Screen("detail/{effect}")
}