package com.tomaszrykala.recsandfx.ui.screen.effect_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomaszrykala.recsandfx.ShowSnackbar
import com.tomaszrykala.recsandfx.data.Effect
import com.tomaszrykala.recsandfx.data.juceEffects
import com.tomaszrykala.recsandfx.ui.theme.paddingLarge
import com.tomaszrykala.recsandfx.ui.theme.paddingMedium
import com.tomaszrykala.recsandfx.ui.theme.paddingXXLarge

@Preview(showBackground = true)
@Composable
fun EffectDetailScreen(
    viewModel: EffectDetailViewModel = viewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    contentPadding: PaddingValues = PaddingValues(),
    effectName: String = juceEffects[0].name,
) {
    val effect = viewModel.getEffect(effectName) // TODO CSQ Flow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(effect)
        Controls(effect)
        Spacer(modifier = Modifier.height(paddingXXLarge))
        RecordButton(viewModel, effectName, snackbarHostState)
        Spacer(modifier = Modifier.height(paddingXXLarge))

        val recordings = viewModel.getFiles(effectName)
        if (recordings.isEmpty()) {
            BigText("Record yourself to discover the effect!")
        } else {
            Recordings(contentPadding, viewModel, snackbarHostState, recordings)
        }
    }
}

@Composable
private fun Title(effect: Effect) {
    BigText(effect.name)
}

@Composable
private fun BigText(text: String) {
    Text(
        modifier = Modifier
            .padding(bottom = paddingMedium)
            .fillMaxWidth(),
        text = text,
        style = TextStyle(
            fontSize = TextUnit(
                value = 24.0F,
                type = TextUnitType.Sp,
            ),
            fontWeight = FontWeight.Bold
        )
    )
}

@Composable
private fun Controls(effect: Effect) {
    effect.params.forEach { param ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = param.name)
            Text(
                text = param.defaultValue.toString(),
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
            Text(text = param.minValue.toString())
            // TODO CSQ Add Slider
            Text(text = param.maxValue.toString())
        }
    }
}

@Composable
private fun RecordButton(
    viewModel: EffectDetailViewModel,
    effectName: String,
    snackbarHostState: SnackbarHostState
) {
    var isRecording by rememberSaveable { mutableStateOf(false) }
    var hasRecordingStarted by rememberSaveable { mutableStateOf(false) }

    if (isRecording) {
        ShowSnackbar(snackbarHostState, "You're recording!")
    } else if (hasRecordingStarted) {
        ShowSnackbar(snackbarHostState, "You've stopped recording!")
    } // TODO after the recording the list doesn't update

    IconButton(
        onClick = {
            if (isRecording) {
                viewModel.stopAudioRecorder(effectName)
                isRecording = false
            } else {
                viewModel.startAudioRecorder()
                hasRecordingStarted = true
                isRecording = true
            }
        },
        modifier = Modifier
            .background(
                color = if (isRecording) Color.Red else Color.Yellow,
                shape = RoundedCornerShape(paddingLarge)
            )
            .padding(paddingLarge)
    ) {
        Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
    }
}

@Composable
private fun Recordings(
    contentPadding: PaddingValues,
    viewModel: EffectDetailViewModel,
    snackbarHostState: SnackbarHostState,
    recordings: List<String>
) {
    val context = LocalContext.current
    var selectedRecording by remember { mutableStateOf("") }
    if (selectedRecording != "") {
        ShowSnackbar(snackbarHostState, "Playing $selectedRecording.")
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(paddingMedium),
    ) {
        items(recordings) { recording ->
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (selectedRecording == recording) Color.Yellow else Color.Transparent)
                    .clickable {
                        selectedRecording = if (selectedRecording == recording) "" else recording
                        viewModel.onSelectedRecording(context, selectedRecording)
                    }
                    .padding(paddingMedium),
                text = recording,
                style = TextStyle(fontWeight = FontWeight.Bold)
            )
        }
    }
}
