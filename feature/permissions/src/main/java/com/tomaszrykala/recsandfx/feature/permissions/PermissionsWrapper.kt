package com.tomaszrykala.recsandfx.feature.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

interface PermissionsWrapper {
    fun isRecordingAudioGranted(context: Context): Boolean
    fun getAllPermissions(): List<String>
}

internal class PermissionsWrapperImpl : PermissionsWrapper {

    override fun isRecordingAudioGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context, Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    override fun getAllPermissions(): List<String> = mutableListOf(
        Manifest.permission.RECORD_AUDIO,
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.READ_MEDIA_AUDIO)
        } else {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }
}