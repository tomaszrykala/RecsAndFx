package com.tomaszrykala.recsandfx.data

import android.net.Uri
import android.os.Environment
import android.util.Log
import java.io.File

interface FileStorage {
    fun getRecordingFilePath(effectName: String): String
    fun getAllRecordings(effectName: String): List<String>
    fun getRecordingUri(selectedRecording: String): Uri
}

class FileStorageImpl : FileStorage {

    override fun getRecordingFilePath(effectName: String): String {
        val time = System.currentTimeMillis()
        val filePath = FILE_PREFIX + "${effectName}_$time" + FILE_EXTENSION
        return getRecordingFile(filePath).path
    }

    override fun getAllRecordings(effectName: String): List<String> {
        val file = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
        Log.d("CSQ", "getFiles $file.")
        val files = file.listFiles { _, name -> name.contains(effectName) }
        Log.d("CSQ", "getFiles isDirectory $files.")
        return files?.map { it.name }?.toList() ?: emptyList() // why not File or absolutePath?
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