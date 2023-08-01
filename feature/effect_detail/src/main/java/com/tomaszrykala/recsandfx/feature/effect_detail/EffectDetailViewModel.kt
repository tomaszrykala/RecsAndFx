package com.tomaszrykala.recsandfx.feature.effect_detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import com.tomaszrykala.recsandfx.core.storage.FileStorage
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class EffectDetailViewModel(
    private val fileStorage: FileStorage,
    private val recordingsPlayer: RecordingsPlayer,
    private val effectsRepository: EffectsRepository,
    private val nativeInterface: NativeInterfaceWrapper,
) : ViewModel() {

    private val stateFlow = MutableStateFlow<EffectDetailState>(EffectDetailState.Empty)
    val uiStateFlow: StateFlow<EffectDetailState> = stateFlow

    suspend fun getEffect(effectName: String) {
        val effect: Effect? = effectsRepository.getAllEffects().find { it.name == effectName }
        if (effect != null) {
            nativeInterface.addEffect(effect)
            val recordings: List<String> = fileStorage.getAllRecordings(effect.name)
            stateFlow.emit(EffectDetailState.EffectDetail(effect, recordings))
        } else {
            stateFlow.emit(EffectDetailState.Error)
        }
    }

    suspend fun startAudioRecorder() {
        nativeInterface.startAudioRecorder()
    }

    suspend fun stopAudioRecorder(effect: Effect) {
        nativeInterface.stopAudioRecorder()
        nativeInterface.writeFile(fileStorage.getRecordingFilePath(effect.name))
    }

    suspend fun onSelectedRecording(context: Context, selectedRecording: String) {
        if (selectedRecording.isEmpty()) {
            Log.d(TAG, "Selected Recording: STOP.")
        } else {
            Log.d(TAG, "Selected Recording: $selectedRecording.")
            val uri = fileStorage.getRecordingUri(selectedRecording)
            recordingsPlayer.play(context, uri)
        }
    }

    suspend fun onRecordingStop() = recordingsPlayer.stop()

    suspend fun deleteRecording(selectedRecording: String) {
        if (fileStorage.deleteRecording(selectedRecording)) {
            Log.d(TAG, "Deleted Recording: $selectedRecording.")
        } else {
            Log.d(TAG, "Failed to delete Recording: $selectedRecording.")
        }
    }

    suspend fun onParamChange(effect: Effect, value: Float, index: Int) {
        nativeInterface.updateParamsAt(effect, value, index)
    }

    companion object {
        private const val TAG = "EffectDetailViewModel"
    }
}

sealed class EffectDetailState {
    object Empty : EffectDetailState()
    object Error : EffectDetailState()
    data class EffectDetail(val effect: Effect, val recordings: List<String>) : EffectDetailState()
}
