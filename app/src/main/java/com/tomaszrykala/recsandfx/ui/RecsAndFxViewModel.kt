package com.tomaszrykala.recsandfx.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper

class RecsAndFxViewModel(
    private val nativeInterface: NativeInterfaceWrapper
) : ViewModel() {

    private var isAudioEnabled = false

    fun onCreated(context: Context) {
        val recordAudioPermission =
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
        if (recordAudioPermission == PackageManager.PERMISSION_GRANTED) {
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

    fun getPermissions(): List<String> = mutableListOf(
        Manifest.permission.RECORD_AUDIO,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}