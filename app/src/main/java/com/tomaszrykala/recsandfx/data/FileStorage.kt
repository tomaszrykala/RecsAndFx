package com.tomaszrykala.recsandfx.data

import android.net.Uri
import android.os.Environment
import java.io.File

interface FileStorage {
    fun getRecordingFilePath(effectName: String): String
    fun getAllRecordingsFilePath(effectName: String): String
    fun getRecordingUri(selectedRecording: String): Uri
}

class FileStorageImpl : FileStorage {

    override fun getRecordingFilePath(effectName: String): String {
        val time = System.currentTimeMillis()
        val filePath = FILE_PREFIX + "${effectName}_$time" + FILE_EXTENSION
        return getRecordingFile(filePath).path
    }

    override fun getAllRecordingsFilePath(effectName: String): String {
        val filePath = FILE_PREFIX + "${effectName}_"
        return getRecordingFile(filePath).path
    }

    override fun getRecordingUri(selectedRecording: String): Uri {
        val recordingFile = getRecordingFile(selectedRecording)
        // val toURI: URI = recordingFile.toURI()
        return Uri.fromFile(recordingFile)
    }

    private fun getRecordingFile(fileName: String) = File(
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
        fileName
    )

    private companion object {
        const val FILE_PREFIX = "RecsAndFx_recording_"
        const val FILE_EXTENSION = ".wav"
    }
}