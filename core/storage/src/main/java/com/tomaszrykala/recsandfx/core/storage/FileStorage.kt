package com.tomaszrykala.recsandfx.core.storage

import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File

interface FileStorage {
    fun getRecordingFilePath(effectName: String): String
    fun getAllRecordings(effectName: String): List<String>
    suspend fun getRecordingUri(selectedRecording: String): Uri
    fun deleteRecording(recording: String): Boolean
}

internal class FileStorageImpl : FileStorage {

    override fun getRecordingFilePath(effectName: String): String {
        val time = System.currentTimeMillis()
        val filePath = FILE_PREFIX + "${effectName}_$time" + FILE_EXTENSION
        return getRecordingFile(filePath).path
    }

    override fun getAllRecordings(effectName: String): List<String> {
        val files = storageDirectory().listFiles { _, name -> name.contains(effectName) }
        Log.d(TAG, "getAllRecordings dir: $files.")
        return files?.map { it.name }?.toList() ?: emptyList() // why not File or absolutePath?
    }

    override suspend fun getRecordingUri(selectedRecording: String): Uri {
        val recordingFile = getRecordingFile(selectedRecording)
        return Uri.fromFile(recordingFile)
    }

    override fun deleteRecording(recording: String): Boolean {
        val recordingFile = getRecordingFile(recording)
        return recordingFile.delete() // This will not work as we need scoped storage.
    }

    private fun getRecordingFile(fileName: String) = File(storageDirectory(), fileName)

    // TODO Replace with MediaStore.Audio, to enable deleting.
    private fun storageDirectory(): File =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

    private companion object {
        const val FILE_PREFIX = "RecsAndFx_recording_"
        const val FILE_EXTENSION = ".wav"
        const val TAG = "FileStorage"
    }
}