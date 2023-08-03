package com.tomaszrykala.recsandfx.feature.effect_detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import com.tomaszrykala.recsandfx.core.domain.native.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.core.domain.repository.EffectsRepository
import com.tomaszrykala.recsandfx.core.storage.FileStorage
import com.tomaszrykala.recsandfx.feature.media_player.RecordingsPlayer
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.withContext

class EffectDetailViewModel(
    private val fileStorage: FileStorage,
    private val recordingsPlayer: RecordingsPlayer,
    private val effectsRepository: EffectsRepository,
    private val nativeInterface: NativeInterfaceWrapper,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val defaultDispatcher: CoroutineDispatcher = Dispatchers.Default
) : ViewModel() {

    private lateinit var effect: Effect
    private val stateFlow = MutableStateFlow<EffectDetailUiState>(EffectDetailUiState.Empty)
    val uiStateFlow: StateFlow<EffectDetailUiState> = stateFlow

    suspend fun observeEffect(effectName: String) {
        withContext(defaultDispatcher) {
            val currentEffect: Effect? = effectsRepository.getAllEffects().find { it.name == effectName }
            if (currentEffect != null) {
                effect = currentEffect
                nativeInterface.addEffect(currentEffect)
                emitEffectState(withContext(ioDispatcher) {
                    fileStorage.getAllRecordings(currentEffect.name)
                })
            } else {
                stateFlow.emit(EffectDetailUiState.Error)
            }
        }
    }

    suspend fun startAudioRecorder() =
        withContext(defaultDispatcher) { nativeInterface.startAudioRecorder() }

    suspend fun stopAudioRecorder() {
        withContext(defaultDispatcher) {
            with(nativeInterface) {
                removeEffect()
                stopAudioRecorder()
                emitEffectState(withContext(ioDispatcher) {
                    writeFile(fileStorage.getRecordingFilePath(effect.name))
                    fileStorage.getAllRecordings(effect.name)
                })
            }
        }
    }

    private suspend fun emitEffectState(recordings: List<String>) {
        stateFlow.emit(EffectDetailUiState.EffectDetail(effect, recordings))
    }

    suspend fun onSelectedRecording(context: Context, selectedRecording: String) {
        if (selectedRecording.isNotEmpty()) {
            Log.d(TAG, "Selected Recording: $selectedRecording.")
            val uri = withContext(ioDispatcher) { fileStorage.getRecordingUri(selectedRecording) }
            withContext(defaultDispatcher) { recordingsPlayer.play(context, uri) }
        }
    }

    suspend fun onRecordingStop() = withContext(defaultDispatcher) { recordingsPlayer.stop() }

    suspend fun deleteRecording(selectedRecording: String) {
        if (withContext(ioDispatcher) { fileStorage.deleteRecording(selectedRecording) }) {
            Log.d(TAG, "Deleted Recording: $selectedRecording.")
            emitEffectState(withContext(ioDispatcher) { fileStorage.getAllRecordings(effect.name) })
        } else {
            Log.d(TAG, "Failed to delete Recording: $selectedRecording.")
        }
    }

    suspend fun onParamChange(value: Float, index: Int) =
        withContext(defaultDispatcher) { nativeInterface.updateParamsAt(effect, value, index) }

    companion object {
        private const val TAG = "EffectDetailViewModel"
    }
}