package com.tomaszrykala.recsandfx.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import com.tomaszrykala.recsandfx.TAG

interface RecordingsPlayer {
    fun play(context: Context, uri: Uri)
    fun stop()
}

class RecordingsPlayerImpl : RecordingsPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun play(context: Context, uri: Uri) {
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, uri)
            setOnPreparedListener {
                Log.d(TAG, "$TAG_MP prepared.")
                it?.start()
            }
            setOnCompletionListener {
                Log.d(TAG, "$TAG_MP completed.")
                stop()
            }
            prepareAsync()
        }
    }

    override fun stop() {
        mediaPlayer?.let {
            it.stop()
            it.release()
            Log.d(TAG, "$TAG_MP released.")
        }
        mediaPlayer = null
    }

    companion object {
        private const val TAG_MP = "MediaPlayer"
    }
}