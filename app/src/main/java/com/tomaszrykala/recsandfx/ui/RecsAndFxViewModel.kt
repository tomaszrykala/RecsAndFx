package com.tomaszrykala.recsandfx.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.native.NativeInterfaceWrapperImpl

class RecsAndFxViewModel(
    private val nativeInterface: NativeInterfaceWrapper = NativeInterfaceWrapperImpl()
) : ViewModel() {

    fun onPause() { // TODO This is a HACK! Do in composable, repeat on lifecycle?
        nativeInterface.destroyAudioEngine()
    }

    fun onResume(context: Context) { // TODO app context pass in the constructor
        if (ContextCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            nativeInterface.createAudioEngine()
            nativeInterface.enable(true)
        }
    }

    fun enableAudio(audioEnabled: Boolean) {
        nativeInterface.enable(audioEnabled)
    }
}