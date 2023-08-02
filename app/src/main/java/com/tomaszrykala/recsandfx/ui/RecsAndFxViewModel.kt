package com.tomaszrykala.recsandfx.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper

class RecsAndFxViewModel(
    private val nativeInterface: NativeInterfaceWrapper
) : ViewModel() {

    private var isAudioEnabled = false

    fun onCreated(context: Context) {
        if (ContextCompat.checkSelfPermission(
                context.applicationContext, Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            nativeInterface.createAudioEngine()
            nativeInterface.enable(isAudioEnabled)
        }
    }

    fun onDestroyed() {
        nativeInterface.destroyAudioEngine()
    }

    fun enableAudio(audioEnabled: Boolean) {
        isAudioEnabled = audioEnabled
        nativeInterface.enable(isAudioEnabled)
    }
}