package com.tomaszrykala.recsandfx.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecsAndFxViewModel(
    private val nativeInterface: NativeInterfaceWrapper,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private var isAudioEnabled = false

    suspend fun onStart(context: Context) {
        withContext(defaultDispatcher) {
            if (ContextCompat.checkSelfPermission(
                    context.applicationContext, Manifest.permission.RECORD_AUDIO
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                nativeInterface.createAudioEngine()
                nativeInterface.enable(isAudioEnabled)
            }
        }
    }

    suspend fun onStop() {
        withContext(defaultDispatcher) {
            nativeInterface.destroyAudioEngine()
        }
    }

    suspend fun enableAudio(audioEnabled: Boolean) {
        withContext(defaultDispatcher) {
            isAudioEnabled = audioEnabled
            nativeInterface.enable(isAudioEnabled)
        }
    }
}