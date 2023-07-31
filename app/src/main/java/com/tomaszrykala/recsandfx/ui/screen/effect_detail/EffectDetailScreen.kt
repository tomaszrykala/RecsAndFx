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
import androidx.compose.material3.Slider
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.tomaszrykala.recsandfx.R
import com.tomaszrykala.recsandfx.coredata.Effect
import com.tomaszrykala.recsandfx.coredata.juceEffects
import com.tomaszrykala.recsandfx.ui.ShowSnackbar
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
    val effect: Effect = viewModel.getEffect(effectName) ?: return // TODO CSQ Flow

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Title(effect)
        Controls(effect, viewModel)
        Spacer(modifier = Modifier.height(paddingXXLarge))
        RecordButton(viewModel, snackbarHostState)
        Spacer(modifier = Modifier.height(paddingXXLarge))

        val recordings = viewModel.getFiles()
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
private fun Controls(
    effect: Effect, viewModel: EffectDetailViewModel
) {
    effect.params.forEachIndexed { index, param ->
        var sliderPosition by rememberSaveable { mutableStateOf(param.defaultValue) }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = param.name, modifier = Modifier.padding(end = 4.dp))
            Text(
                text = param.defaultValue.toString(),
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(text = param.minValue.toString(), modifier = Modifier.padding(end = 4.dp))
            Slider(
                value = sliderPosition,
                onValueChange = { value -> sliderPosition = value },
                onValueChangeFinished = { viewModel.onParamChange(sliderPosition, index) },
                valueRange = object : ClosedFloatingPointRange<Float> {
                    override fun lessThanOrEquals(a: Float, b: Float): Boolean = a <= b
                    override val endInclusive: Float = param.maxValue
                    override val start: Float = param.minValue
                },
                modifier = Modifier
                    .weight(0.5f, false)
                    .padding(end = 4.dp)
            )
            Text(text = param.maxValue.toString())
        }
    }
}

@Composable
private fun RecordButton(
    viewModel: EffectDetailViewModel,
    snackbarHostState: SnackbarHostState
) {
    var isRecording by rememberSaveable { mutableStateOf(false) }
    var hasRecordingStarted by rememberSaveable { mutableStateOf(false) }

    if (isRecording) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.you_re_recording))
    } else if (hasRecordingStarted) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.you_ve_stopped_recording))
    } // TODO after the recording the list doesn't update

    IconButton(
        onClick = {
            if (isRecording) {
                viewModel.stopAudioRecorder()
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
        ShowSnackbar(
            snackbarHostState, stringResource(R.string.playing_recording, selectedRecording)
        )
    }
//    var deletedRecording by remember { mutableStateOf("") }
//    if (deletedRecording != "") {
//        ShowSnackbar(
//            snackbarHostState, stringResource(R.string.deleted_recording, deletedRecording)
//        )
//    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        items(recordings) { recording ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (selectedRecording == recording) Color.Yellow else Color.White)
                    .padding(paddingMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .clickable {
                            selectedRecording =
                                if (selectedRecording == recording) "" else recording
                            viewModel.onSelectedRecording(context, selectedRecording)
                        },
                    text = recording,
                    style = TextStyle(fontWeight = FontWeight.Bold),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                // Delete not working yet
//                IconButton(
//                    onClick = {
//                        deletedRecording = recording
//                        viewModel.deleteRecording(recording)
//                    }) {
//                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
//                }
            }
        }
    }
}