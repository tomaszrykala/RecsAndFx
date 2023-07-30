package com.tomaszrykala.recsandfx.ui.screen.effect_detail

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.tomaszrykala.recsandfx.data.Effect
import com.tomaszrykala.recsandfx.data.FileStorage
import com.tomaszrykala.recsandfx.data.FileStorageImpl
import com.tomaszrykala.recsandfx.data.NativeInterfaceWrapper
import com.tomaszrykala.recsandfx.data.NativeInterfaceWrapperImpl
import com.tomaszrykala.recsandfx.data.juceEffects
import com.tomaszrykala.recsandfx.oboeRealFx
import com.tomaszrykala.recsandfx.player.RecordingsPlayer
import com.tomaszrykala.recsandfx.player.RecordingsPlayerImpl

class EffectDetailViewModel(
    private val nativeInterface: NativeInterfaceWrapper = NativeInterfaceWrapperImpl(),
    private val fileStorage: FileStorage = FileStorageImpl(),
    private val recordingsPlayer: RecordingsPlayer = RecordingsPlayerImpl(),
) : ViewModel() {

    // effectName should really be a field?

    fun getEffect(effectName: String): Effect {
        return (oboeRealFx.find { it.name == effectName } ?: juceEffects[0]) // TODO CSQ debug
    }

    fun startAudioRecorder() {
        nativeInterface.startAudioRecorder()
    }

    fun stopAudioRecorder(effectName: String) {
        nativeInterface.stopAudioRecorder()
        nativeInterface.writeFile(fileStorage.getRecordingFilePath(effectName))
    }

    fun getFiles(effectName: String): List<String> = fileStorage.getAllRecordings(effectName)

    fun onSelectedRecording(context: Context, selectedRecording: String) {
        // https://developer.android.com/guide/topics/media/platform/mediaplayer
        // https://proandroiddev.com/learn-with-code-jetpack-compose-playing-media-part-3-3792bdfbe1ea
        if (selectedRecording.isEmpty()) {
            Log.d("CSQ", "Selected Recording: STOP.")
        } else {
            Log.d("CSQ", "Selected Recording: $selectedRecording.")
            val uri = fileStorage.getRecordingUri(selectedRecording)
            recordingsPlayer.play(context, uri)
        }
    }

    fun onRecordingStop() = recordingsPlayer.stop()
}