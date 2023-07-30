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
        nativeInterface.writeFile(fileStorage.getRecordingFilePath(effectName))
    }

    fun getFiles(): List<String> {
        // val filePath = fileStorage.getAllRecordingsFilePath(effectName)

        // /sdcard/Music/RecsAndFx_recording_Echo_1690732892215.wav
        // /sdcard/Music/RecsAndFx_recording_Tremolo_1690732117397.wav
        return listOf( // TODO CSQ DEBUG
            "RecsAndFx_recording_Echo_1690732892215.wav",
            "RecsAndFx_recording_Tremolo_1690732117397.wav"
        )
    }

    fun onSelectedRecording(
        context: Context,
        selectedRecording: String
    ) {
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
}