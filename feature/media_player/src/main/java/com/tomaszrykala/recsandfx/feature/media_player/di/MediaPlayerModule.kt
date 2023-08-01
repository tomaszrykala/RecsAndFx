package com.tomaszrykala.recsandfx.feature.media_player.di

import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayer
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayerImpl
import org.koin.dsl.module

val mediaPlayerModule = module {

    single<RecordingsPlayer> { RecordingsPlayerImpl() }
}