package com.tomaszrykala.recsandfx.feature.permissions.di

import com.tomaszrykala.recsandfx.feature.permissions.PermissionsWrapper
import com.tomaszrykala.recsandfx.feature.permissions.PermissionsWrapperImpl
import org.koin.dsl.module

val permissionsModule = module {

    factory<PermissionsWrapper> { PermissionsWrapperImpl() }
}