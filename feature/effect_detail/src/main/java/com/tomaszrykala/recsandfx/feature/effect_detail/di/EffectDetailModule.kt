package com.tomaszrykala.recsandfx.feature.effect_detail.di

import com.tomaszrykala.recsandfx.feature.effect_detail.EffectDetailViewModel
import com.tomaszrykala.recsandfx.feature.effect_detail.di.DispatcherQualifier.*
import kotlinx.coroutines.Dispatchers
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val effectDetailModule = module {

    viewModel {
        EffectDetailViewModel(
            fileStorage = get(),
            recordingsPlayer = get(),
            effectsRepository = get(),
            nativeInterface = get(),
            ioDispatcher = get(named(IO.name)),
            defaultDispatcher = get(named(Default.name))
        )
    }

    single(named(IO.name)) {
        Dispatchers.IO
    }

    single(named(Default.name)) {
        Dispatchers.Default
    }
}

private enum class DispatcherQualifier {
    IO, Default
}