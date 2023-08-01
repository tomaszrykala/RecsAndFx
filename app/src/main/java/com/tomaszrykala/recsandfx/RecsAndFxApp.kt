package com.tomaszrykala.recsandfx

import android.app.Application
import com.tomaszrykala.recsandfx.core.domain.di.domainModule
import com.tomaszrykala.recsandfx.di.appModule
import com.tomaszrykala.recsandfx.feature.effect_detail.di.effectDetailModule
import com.tomaszrykala.recsandfx.feature.effects_list.di.effectsListModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class RecsAndFxApp : Application() {

    override fun onCreate() = super.onCreate().also {
        startKoinDi()
    }

    private fun startKoinDi() {
        startKoin {
            androidContext(this@RecsAndFxApp)
            androidLogger()
            modules(
                appModule,
                domainModule,
                effectsListModule,
                effectDetailModule,
            )
        }
    }
}

const val TAG = "RecsAndFxApp"