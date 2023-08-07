package com.tomaszrykala.recsandfx

import android.content.Context
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.feature.permissions.PermissionsWrapper

class RecsAndFxViewModel(
    private val nativeInterface: NativeInterfaceWrapper,
    private val permissionsWrapper: PermissionsWrapper,
) : ViewModel() {

    private var isAudioEnabled = false

    fun onCreated(context: Context) {
        if (permissionsWrapper.isRecordingAudioGranted(context)) {
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

    fun getPermissions(): List<String> = permissionsWrapper.getAllPermissions()
}