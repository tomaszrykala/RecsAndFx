package com.tomaszrykala.recsandfx.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.data.NativeInterface
import com.tomaszrykala.recsandfx.data.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.data.NativeInterfaceWrapperImpl

class RecsAndFxViewModel(
    private val nativeInterfaceWrapper: NativeInterfaceWrapper = NativeInterfaceWrapperImpl()
) : ViewModel() {

    fun onPause() { // TODO This is a HACK! Do in composable, repeat on lifecycle?
        nativeInterfaceWrapper.destroyAudioEngine()
    }

    fun onResume(context: Context) { // TODO app context pass in the constructor
        if (ContextCompat.checkSelfPermission(
                context.applicationContext,
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            NativeInterface.createAudioEngine()
            NativeInterface.enable(true)
        }
    }
}