package com.tomaszrykala.recsandfx.feature.effect_detail.di

import com.tomaszrykala.recsandfx.feature.effect_detail.EffectDetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val effectDetailModule = module {

    viewModel {
        EffectDetailViewModel(
            fileStorage = get(),
            recordingsPlayer = get(),
            effectsRepository = get(),
            nativeInterface = get(),
        )
    }
}