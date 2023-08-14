package com.tomaszrykala.recsandfx.feature.effect_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import kotlin.math.pow
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Preview(showBackground = true)
@Composable
fun EffectDetailScreen(
    viewModel: EffectDetailViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    windowSizeClass: WindowSizeClass = WindowSizeClass.calculateFromSize(DpSize.Unspecified),
    contentPadding: PaddingValues = PaddingValues(),
    effectName: String = "Delay",
) {
    val coroutineScope = rememberCoroutineScope()
    val state = with(viewModel) {
        coroutineScope.launch { observeEffect(effectName) }
        uiStateFlow.collectAsStateWithLifecycle()
    }
    val paddingValues = PaddingValues(
        top = contentPadding.calculateTopPadding() + paddingMedium,
        bottom = contentPadding.calculateBottomPadding(),
        start = paddingMedium,
        end = paddingMedium
    )

    when (state.value) {
        is EffectDetailUiState.EffectDetail -> {
            val effectDetail = state.value as EffectDetailUiState.EffectDetail
            if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact) {
                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                    Title(stringResource(R.string.screen_too_small_error))
                } else {
                    ControlsAndRecordings(paddingValues, snackbarHostState, viewModel, effectDetail, false)
                }
            } else {
                ControlsAndRecordings(paddingValues, snackbarHostState, viewModel, effectDetail, true)
            }
        }

        EffectDetailUiState.Empty -> {
            val message = stringResource(R.string.recordings_loading)
            LaunchedEffect(message) { snackbarHostState.showSnackbar(message) }
        }

        is EffectDetailUiState.Error -> {
            Title(stringResource((state.value as EffectDetailUiState.Error).errorResId))
        }
    }
}

@Composable
private fun ControlsAndRecordings(
    paddingValues: PaddingValues,
    snackbarHostState: SnackbarHostState,
    viewModel: EffectDetailViewModel, // TODO pass lambdas, not the VM
    detail: EffectDetailUiState.EffectDetail,
    isPortrait: Boolean,
) {
    val (effect, recordings) = detail

    val parentModifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .padding(paddingValues)

    if (isPortrait) {
        Column(
            modifier = parentModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ControlsSection(effect, viewModel, snackbarHostState, isPortrait)
            RecordingsSection(recordings, viewModel, snackbarHostState)
        }
    } else {
        Row(
            modifier = parentModifier,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(0.5f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) { ControlsSection(effect, viewModel, snackbarHostState, false) }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(0.5f, false)
                    .padding(start = paddingMedium)
            )
            { RecordingsSection(recordings, viewModel, snackbarHostState) }
        }
    }
}

@Composable
private fun ControlsSection(
    effect: Effect,
    viewModel: EffectDetailViewModel,
    snackbarHostState: SnackbarHostState,
    isPortrait: Boolean
) {
    Title(effect.name)
    Text(modifier = Modifier.padding(top = paddingMedium), text = stringResource(effect.description))
    ContentSpacer(isPortrait)
    Controls(effect, viewModel)
    ContentSpacer(isPortrait)
    RecordButton(
        isPortrait = isPortrait,
        snackbarHostState = snackbarHostState,
        onRecordingStop = { viewModel.stopAudioRecorder() },
        onRecordingStart = { viewModel.startAudioRecorder() },
    )
    ContentSpacer(isPortrait)
}

@Composable
private fun RecordingsSection(
    recordings: List<String>,
    viewModel: EffectDetailViewModel,
    snackbarHostState: SnackbarHostState
) {
    if (recordings.isEmpty()) {
        Title(stringResource(R.string.empty_recordings_state_prompt))
    } else {
        Recordings(viewModel, snackbarHostState, recordings)
    }
}

@Composable
private fun ContentSpacer(isPortrait: Boolean) {
    Spacer(modifier = Modifier.height(if (isPortrait) paddingXLarge else paddingMedium))
}

@Composable
private fun Title(text: String) {
    Text(
        modifier = Modifier
            .padding(bottom = paddingMedium)
            .fillMaxWidth(),
        text = text,
        style = TextStyle(
            fontSize = spTextUnit(24.0f),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
    )
}

@Composable
fun spTextUnit(size: Float) = TextUnit(value = size, type = TextUnitType.Sp)

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
            Text(
                text = param.name,
                modifier = Modifier.padding(end = paddingSmall),
                style = TextStyle(fontSize = spTextUnit(18.0f))
            )
            Text(
                text = sliderPosition.roundToTwoDecimals().toString(),
                modifier = Modifier.padding(end = paddingSmall),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = spTextUnit(18.0f))
            )
            Text(
                text = param.minValue.toString(), modifier = Modifier
                    .alpha(0.6f)
                    .padding(end = paddingSmall)
            )
            Slider(
                value = sliderPosition,
                onValueChange = { value -> sliderPosition = value },
                onValueChangeFinished = {
                    coroutineScope.launch { viewModel.onParamChange(sliderPosition, index) }
                },
                valueRange = object : ClosedFloatingPointRange<Float> {
                    override fun lessThanOrEquals(a: Float, b: Float): Boolean = a <= b
                    override val endInclusive: Float = param.maxValue
                    override val start: Float = param.minValue
                },
                modifier = Modifier
                    .weight(0.5f, false)
                    .padding(end = paddingSmall),
            )
            Text(text = param.maxValue.toString(), modifier = Modifier.alpha(0.6f))
        }
    }
}

@Composable
private fun RecordButton(
    isPortrait: Boolean = false,
    snackbarHostState: SnackbarHostState,
    onRecordingStop: suspend () -> Unit,
    onRecordingStart: suspend () -> Unit
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
                    onRecordingStop.invoke()
                    isRecording = false
                } else {
                    onRecordingStart.invoke()
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

@Composable
private fun Recordings(
    viewModel: EffectDetailViewModel,
    snackbarHostState: SnackbarHostState,
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
                            selectedRecording?.let {
                                viewModel.onSelectedRecording(context, it)
                                snackbarHostState.showSnackbar(selectedRecordingMsg.format(it))
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
                            viewModel.deleteRecording(recording)
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

private val paddingXLarge = 32.dp
private val paddingLarge = 16.dp
private val paddingMedium = 8.dp
private val paddingSmall = 4.dp

private fun Float.roundToTwoDecimals(): Double {
    val factor = 10.0.pow(2.toDouble())
    return (this * factor).roundToInt() / factor
}
