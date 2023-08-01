package com.tomaszrykala.recsandfx.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper

class RecsAndFxViewModel(
    private val nativeInterface: NativeInterfaceWrapper,
) : ViewModel() {

    private var isAudioEnabled = false

    suspend fun onStop() {
        nativeInterface.destroyAudioEngine()
    }

    suspend fun onStart(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context.applicationContext, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            nativeInterface.createAudioEngine()
            nativeInterface.enable(isAudioEnabled)
        }
    }

    suspend fun enableAudio(audioEnabled: Boolean) {
        isAudioEnabled = audioEnabled
        nativeInterface.enable(isAudioEnabled)
    }
}