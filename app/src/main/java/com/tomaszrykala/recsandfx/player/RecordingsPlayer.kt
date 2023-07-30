package com.tomaszrykala.recsandfx.player

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log

interface RecordingsPlayer {
    fun play(context: Context, uri: Uri)
    fun stop()
}

class RecordingsPlayerImpl : RecordingsPlayer {

    private var mediaPlayer: MediaPlayer? = null

    override fun play(context: Context, uri: Uri) {
        // sync
        // MediaPlayer.create(context, uri).start()

        // async
        mediaPlayer = MediaPlayer().apply {
            setDataSource(context, uri)
            setOnPreparedListener {
                Log.d("CSQ", "MediaPlayer prepared.")
                it?.start()
            }
            setOnCompletionListener {
                Log.d("CSQ", "MediaPlayer completed.")
                stop()
            }
            prepareAsync()
        }
    }

    override fun stop() {
        mediaPlayer?.let {
            it.stop()
            it.release()
            Log.d("CSQ", "MediaPlayer released.")
        }
        mediaPlayer = null
    }
}