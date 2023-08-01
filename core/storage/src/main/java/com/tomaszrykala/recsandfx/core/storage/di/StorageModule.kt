package com.tomaszrykala.recsandfx.core.storage.di

import com.tomaszrykala.recsandfx.core.storage.FileStorage
import com.tomaszrykala.recsandfx.core.storage.FileStorageImpl
import org.koin.dsl.module

val storageModule = module {

    factory<FileStorage> { FileStorageImpl() }
}