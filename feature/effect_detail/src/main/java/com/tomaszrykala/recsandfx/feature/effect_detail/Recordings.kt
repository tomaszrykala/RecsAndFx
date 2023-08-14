package com.tomaszrykala.recsandfx.feature.effect_detail

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import kotlinx.coroutines.launch

@Composable
internal fun Recordings(
    snackbarHostState: SnackbarHostState,
    onSelectedRecording: suspend (Context, String) -> Unit,
    deleteRecording: suspend (String) -> Unit,
    recordings: List<String>
) {
    var selectedRecording: String? by remember { mutableStateOf(null) }
    var deletedRecording by remember { mutableStateOf("") }
    val selectedRecordingMsg = stringResource(R.string.playing_recording)
    val deletedRecordingMsg = stringResource(R.string.deleted_recording)

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val itemUnSelectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(paddingSmall),
    ) {
        items(recordings) { recording ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = if (selectedRecording == recording) Color.Yellow else itemUnSelectedColor,
                        shape = RoundedCornerShape(size = paddingSmall)
                    )
                    .clickable {
                        selectedRecording = if (selectedRecording == recording) null else recording
                        coroutineScope.launch {
                            selectedRecording?.let { recording ->
                                onSelectedRecording(context, recording)
                                snackbarHostState.showSnackbar(selectedRecordingMsg.format(recording))
                            } ?: snackbarHostState.currentSnackbarData?.dismiss()
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = recording,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(start = paddingMedium)
                )
                IconButton(
                    onClick = {
                        deletedRecording = recording
                        coroutineScope.launch {
                            deleteRecording(recording)
                            snackbarHostState.showSnackbar(deletedRecordingMsg.format(recording))
                        }
                    }
                ) {
                    Icon(painterResource(R.drawable.ic_round_delete_24), stringResource(R.string.delete_button))
                }
            }
        }
    }
}