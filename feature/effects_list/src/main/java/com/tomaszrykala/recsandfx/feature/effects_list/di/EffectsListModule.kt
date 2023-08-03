package com.tomaszrykala.recsandfx.feature.effects_list.di

import com.tomaszrykala.recsandfx.feature.effects_list.EffectsListViewModel
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val effectsListModule = module {

    viewModel { EffectsListViewModel(get(), get()) }

    single { Dispatchers.Default }
}