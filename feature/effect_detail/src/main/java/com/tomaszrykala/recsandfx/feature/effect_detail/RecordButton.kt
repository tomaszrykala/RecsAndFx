package com.tomaszrykala.recsandfx.feature.effect_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import kotlinx.coroutines.launch

@Composable
internal fun RecordButton(
    isPortrait: Boolean = false,
    snackbarHostState: SnackbarHostState,
    onRecordingStart: suspend () -> Unit,
    onRecordingStop: suspend () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isRecording by rememberSaveable { mutableStateOf(false) }
    var hasRecordingStarted by rememberSaveable { mutableStateOf(false) }
    val recordingStarted = stringResource(R.string.you_re_recording)
    val recordingStopped = stringResource(R.string.recording_stopped)

    IconButton(
        onClick = {
            coroutineScope.launch {
                if (isRecording) {
                    onRecordingStop()
                    isRecording = false
                } else {
                    onRecordingStart()
                    hasRecordingStarted = true
                    isRecording = true
                }
                if (isRecording) {
                    snackbarHostState.showSnackbar(recordingStarted)
                } else if (hasRecordingStarted) {
                    snackbarHostState.showSnackbar(recordingStopped)
                }
            }
        },
        modifier = Modifier
            .background(
                color = if (isRecording) Color.Red else Color.Yellow,
                shape = RoundedCornerShape(paddingLarge)
            )
            .padding(if (isPortrait) paddingLarge else paddingSmall)
    ) {
        Icon(painterResource(R.drawable.ic_round_mic_24), stringResource(R.string.start_recording))
    }
}