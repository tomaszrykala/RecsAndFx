package com.tomaszrykala.recsandfx.feature.effect_detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapperImpl
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepositoryImpl
import com.tomaszrykala.recsandfx.core.storage.FileStorage
import com.tomaszrykala.recsandfx.core.storage.FileStorageImpl
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayer
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayerImpl

class EffectDetailViewModel(
    private val fileStorage: FileStorage = FileStorageImpl(),
    private val recordingsPlayer: RecordingsPlayer = RecordingsPlayerImpl(),
    private val effectsRepository: EffectsRepository = EffectsRepositoryImpl(),
    private val nativeInterface: NativeInterfaceWrapper = NativeInterfaceWrapperImpl(),
) : ViewModel() {

    private lateinit var effect: Effect

    // TODO FLOW
    fun getEffect(effectName: String): Effect? {
        return effectsRepository.getAllEffects().find { it.name == effectName }?.also {
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

    companion object {
        private const val TAG = "EffectDetailViewModel"
    }
}