package com.tomaszrykala.recsandfx.ui.screen.effect_detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.TAG
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapperImpl
import com.tomaszrykala.recsandfx.core.domain.effect.allEffects
import com.tomaszrykala.recsandfx.core.storage.FileStorage
import com.tomaszrykala.recsandfx.core.storage.FileStorageImpl
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayer
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayerImpl

class EffectDetailViewModel(
    private val nativeInterface: NativeInterfaceWrapper = NativeInterfaceWrapperImpl(),
    private val fileStorage: FileStorage = FileStorageImpl(),
    private val recordingsPlayer: RecordingsPlayer = RecordingsPlayerImpl(),
) : ViewModel() {

    private lateinit var effect: Effect

    fun getEffect(effectName: String): Effect? {
        return allEffects.find { it.name == effectName }?.also {
            nativeInterface.addEffect(it)
            effect = it
        }
    }

    fun startAudioRecorder() {
        nativeInterface.startAudioRecorder()
    }

    fun stopAudioRecorder() {
        nativeInterface.stopAudioRecorder()
        nativeInterface.writeFile(fileStorage.getRecordingFilePath(effect.name))
    }

    fun getFiles(): List<String> = fileStorage.getAllRecordings(effect.name)

    fun onSelectedRecording(context: Context, selectedRecording: String) {
        if (selectedRecording.isEmpty()) {
            Log.d(TAG, "Selected Recording: STOP.")
        } else {
            Log.d(TAG, "Selected Recording: $selectedRecording.")
            val uri = fileStorage.getRecordingUri(selectedRecording)
            recordingsPlayer.play(context, uri)
        }
    }

    fun onRecordingStop() = recordingsPlayer.stop()

    fun deleteRecording(selectedRecording: String) {
        if (fileStorage.deleteRecording(selectedRecording)) {
            Log.d(TAG, "Deleted Recording: $selectedRecording.")
        } else {
            Log.d(TAG, "Failed to delete Recording: $selectedRecording.")
        }
    }

    fun onParamChange(value: Float, index: Int) {
        nativeInterface.updateParamsAt(effect, value, index)
    }
}