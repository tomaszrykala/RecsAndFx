package com.tomaszrykala.recsandfx.feature.effects_list.di

import com.tomaszrykala.recsandfx.feature.effects_list.EffectsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val effectsListModule = module {

    viewModel { EffectsScreenViewModel(get()) }
}