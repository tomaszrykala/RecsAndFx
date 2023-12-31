package com.tomaszrykala.recsandfx.core.storage

import android.net.Uri
import android.os.Environment
import java.io.File

interface FileStorage {
    suspend fun getRecordingFilePath(effectName: String): String
    suspend fun getAllRecordings(effectName: String): List<String>
    suspend fun getRecordingUri(selectedRecording: String): Uri
    suspend fun deleteRecording(recording: String): Boolean
}

internal class FileStorageImpl : FileStorage {

    override suspend fun getRecordingFilePath(effectName: String): String {
        val time = System.currentTimeMillis()
        val filePath = FILE_PREFIX + "${effectName}_$time" + FILE_EXTENSION
        return getRecordingFile(filePath).path
    }

    override suspend fun getAllRecordings(effectName: String): List<String> {
        val files = storageDirectory().listFiles { _, name -> name.contains(effectName) }
        return files?.map { it.name }?.toList() ?: emptyList() // why not File or absolutePath?
    }

    override suspend fun getRecordingUri(selectedRecording: String): Uri {
        val recordingFile = getRecordingFile(selectedRecording)
        return Uri.fromFile(recordingFile)
    }

    override suspend fun deleteRecording(recording: String): Boolean {
        val recordingFile = getRecordingFile(recording)
        return recordingFile.delete() // This _might_ not work without scoped storage or MediaStore.
    }

    private fun getRecordingFile(fileName: String) = File(storageDirectory(), fileName)

    private fun storageDirectory(): File =
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)

    private companion object {
        const val FILE_PREFIX = "RecsAndFx_recording_"
        const val FILE_EXTENSION = ".wav"
    }
}