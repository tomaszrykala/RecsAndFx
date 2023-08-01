package com.tomaszrykala.recsandfx.di

import com.tomaszrykala.recsandfx.ui.RecsAndFxViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    viewModel { RecsAndFxViewModel(get()) }
}