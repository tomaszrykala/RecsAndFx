package com.tomaszrykala.recsandfx.feature.effect_detail

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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.pow
import kotlin.math.roundToInt

@Preview(showBackground = true)
@Composable
fun EffectDetailScreen(
    viewModel: EffectDetailViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    contentPadding: PaddingValues = PaddingValues(),
    effectName: String = "Delay",
) {
    val coroutineScope = rememberCoroutineScope()
    val state = with(viewModel) {
        coroutineScope.launch { observeEffect(effectName) }
        uiStateFlow.collectAsStateWithLifecycle()
    }

    when (state.value) {
        is EffectDetailState.EffectDetail -> {
            val detailState = state.value as EffectDetailState.EffectDetail
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(contentPadding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Title(detailState.effect)
                Controls(detailState.effect, viewModel)
                Spacer(modifier = Modifier.height(paddingXXLarge))
                RecordButton(detailState.effect, viewModel, snackbarHostState)
                Spacer(modifier = Modifier.height(paddingXXLarge))

                if (detailState.recordings.isEmpty()) {
                    BigText("Record yourself to discover the effect!")
                } else {
                    Recordings(contentPadding, viewModel, snackbarHostState, detailState.recordings)
                }
            }
        }

        EffectDetailState.Empty -> ShowSnackbar(snackbarHostState, "Loading...")
        EffectDetailState.Error -> BigText("Error: Couldn't load the effect! Try again please.")
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
        val coroutineScope = rememberCoroutineScope()
        var sliderPosition by rememberSaveable { mutableStateOf(param.defaultValue) }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = param.name, modifier = Modifier.padding(end = 4.dp))
            Text(
                text = sliderPosition.roundToTwoDecimals().toString(),
                style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 4.dp)
            )
            Text(text = param.minValue.toString(), modifier = Modifier.padding(end = 4.dp))
            Slider(
                value = sliderPosition,
                onValueChange = { value -> sliderPosition = value },
                onValueChangeFinished = {
                    coroutineScope.launch { viewModel.onParamChange(effect, sliderPosition, index) }
                },
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
    effect: Effect,
    viewModel: EffectDetailViewModel,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()
    var isRecording by rememberSaveable { mutableStateOf(false) }
    var hasRecordingStarted by rememberSaveable { mutableStateOf(false) }

    if (isRecording) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.you_re_recording))
    } else if (hasRecordingStarted) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.recording_stopped))
    }

    IconButton(
        onClick = {
            coroutineScope.launch {
                if (isRecording) {
                    viewModel.stopAudioRecorder(effect)
                    isRecording = false
                } else {
                    viewModel.startAudioRecorder(effect)
                    hasRecordingStarted = true
                    isRecording = true
                }
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
    var selected by remember { mutableStateOf("") }

    if (selected != "") {
        ShowSnackbar(snackbarHostState, stringResource(R.string.playing_recording, selected))
    }
//    var deletedRecording by remember { mutableStateOf("") }
//    if (deletedRecording != "") {
//        ShowSnackbar(
//            snackbarHostState, stringResource(R.string.deleted_recording, deletedRecording)
//        )
//    }

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        items(recordings) { recording ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(if (selected == recording) Color.Yellow else Color.White)
                    .padding(paddingMedium),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier
                        .clickable {
                            selected = if (selected == recording) "" else recording
                            coroutineScope.launch {
                                viewModel.onSelectedRecording(context, selected)
                            }
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

@Composable
fun ShowSnackbar(snackbarHostState: SnackbarHostState, message: String) {
    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
    }
}

val paddingXXLarge = 64.dp
val paddingLarge = 16.dp
val paddingMedium = 8.dp

fun Float.roundToTwoDecimals(): Double {
    val factor = 10.0.pow(2.toDouble())
    return (this * factor).roundToInt() / factor
}
