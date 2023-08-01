package com.tomaszrykala.recsandfx.feature.effect_detail.di

import com.tomaszrykala.recsandfx.core.storage.FileStorage
import com.tomaszrykala.recsandfx.core.storage.FileStorageImpl
import com.tomaszrykala.recsandfx.feature.effect_detail.EffectDetailViewModel
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayer
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayerImpl
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

    // TODO go to their own modules
    factory<FileStorage> { FileStorageImpl() }
    single<RecordingsPlayer> { RecordingsPlayerImpl() }
}