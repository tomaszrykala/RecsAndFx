package com.tomaszrykala.recsandfx.core.domain.di

import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapperImpl
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepositoryImpl
import org.koin.dsl.module

val domainModule = module {

    factory<NativeInterfaceWrapper> { NativeInterfaceWrapperImpl() }

    single<EffectsRepository> { EffectsRepositoryImpl(get()) }
}