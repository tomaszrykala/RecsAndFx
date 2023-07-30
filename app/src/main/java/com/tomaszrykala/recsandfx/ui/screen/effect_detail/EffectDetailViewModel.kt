package com.tomaszrykala.recsandfx.ui.screen.effect_detail

import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.data.Effect
import com.tomaszrykala.recsandfx.data.FileStorage
import com.tomaszrykala.recsandfx.data.FileStorageImpl
import com.tomaszrykala.recsandfx.data.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.data.NativeInterfaceWrapperImpl
import com.tomaszrykala.recsandfx.data.juceEffects
import com.tomaszrykala.recsandfx.oboeRealFx

class EffectDetailViewModel(
    private val nativeInterface: NativeInterfaceWrapper = NativeInterfaceWrapperImpl(),
    private val fileStorage: FileStorage = FileStorageImpl(),
) : ViewModel() {

    private lateinit var effectName: String

    fun getEffect(effectName: String): Effect {
        return (oboeRealFx.find { it.name == effectName } ?: juceEffects[0]).also {
            this.effectName = it.name
        } // TODO CSQ debug
    }

    fun startAudioRecorder() {
        nativeInterface.startAudioRecorder()
    }

    fun stopAudioRecorder() {
        nativeInterface.stopAudioRecorder()
        nativeInterface.writeFile(fileStorage.getAudioRecordingFilePath(effectName))
    }
}