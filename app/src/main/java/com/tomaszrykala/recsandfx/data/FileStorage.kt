package com.tomaszrykala.recsandfx.data

import android.os.Environment
import java.io.File

interface FileStorage {
    fun getAudioRecordingFilePath(effectName: String): String
}

class FileStorageImpl : FileStorage {

    override fun getAudioRecordingFilePath(effectName: String): String {
        val time = System.currentTimeMillis()
        val filePath = FILE_PREFIX + "${effectName}_$time" + FILE_EXTENSION
        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC),
            filePath
        )
        return file.path
    }

    private companion object {
        const val FILE_PREFIX = "RecsAndFx_recording_"
        const val FILE_EXTENSION = ".wav"
    }
}