package com.tomaszrykala.recsandfx.feature.effect_detail

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.tomaszrykala.recsandfx.core.domain.effect.Effect
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

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
            val hasCompactHeight = windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact
            if (hasCompactHeight && windowSizeClass.widthSizeClass == WindowWidthSizeClass.Compact) {
                Title(stringResource(R.string.screen_too_small_error))
            } else {
                ControlsAndRecordings(
                    paddingValues,
                    snackbarHostState,
                    state.value as EffectDetailUiState.EffectDetail,
                    viewModel,
                    hasCompactHeight.not()
                )
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
    detail: EffectDetailUiState.EffectDetail,
    viewModel: EffectDetailViewModel,
    isPortrait: Boolean,
) {

    val onRecordingStart: suspend () -> Unit = { viewModel.startAudioRecorder() }
    val onRecordingStop: suspend () -> Unit = { viewModel.stopAudioRecorder() }
    val onSliderChange: suspend (Float, Int) -> Unit = { value, index -> viewModel.onParamChange(value, index) }
    val onSelectedRecording: suspend (Context, String) -> Unit =
        { ctx, name -> viewModel.onSelectedRecording(ctx, name) }
    val deleteRecording: suspend (String) -> Unit = { name -> viewModel.deleteRecording(name) }

    val (effect, recordings) = detail
    val slidersStateRememberSaveable = rememberSaveable { createSlidersState(effect) }

    val parentModifier = Modifier
        .fillMaxSize()
        .background(color = MaterialTheme.colorScheme.background)
        .padding(paddingValues)

    if (isPortrait) {
        Column(
            modifier = parentModifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ControlsSection(
                effect,
                snackbarHostState,
                slidersStateRememberSaveable,
                onSliderChange,
                onRecordingStart,
                onRecordingStop,
                true
            )
            RecordingsSection(snackbarHostState, onSelectedRecording, deleteRecording, recordings)
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
            ) {
                ControlsSection(
                    effect,
                    snackbarHostState,
                    slidersStateRememberSaveable,
                    onSliderChange,
                    onRecordingStart,
                    onRecordingStop,
                    false
                )
            }
            Box(
                Modifier
                    .fillMaxSize()
                    .weight(0.5f, false)
                    .padding(start = paddingMedium)
            )
            { RecordingsSection(snackbarHostState, onSelectedRecording, deleteRecording, recordings) }
        }
    }
}

private fun createSlidersState(effect: Effect): Map<Int, MutableState<Float>> {
    return effect.params
        .mapIndexed { index, param -> index to mutableStateOf(param.defaultValue) }
        .associateBy { it.first }.mapValues { it.value.second }
}

@Composable
private fun ControlsSection(
    effect: Effect,
    snackbarHostState: SnackbarHostState,
    slidersState: Map<Int, MutableState<Float>>,
    onSliderChange: suspend (value: Float, index: Int) -> Unit,
    onRecordingStart: suspend () -> Unit,
    onRecordingStop: suspend () -> Unit,
    isPortrait: Boolean
) {
    Title(effect.name)
    Text(modifier = Modifier.padding(top = paddingMedium), text = stringResource(effect.description))
    ContentSpacer(isPortrait)
    Controls(effect, slidersState, onSliderChange)
    ContentSpacer(isPortrait)
    RecordButton(isPortrait, snackbarHostState, onRecordingStart, onRecordingStop)
    ContentSpacer(isPortrait)
}

@Composable
private fun RecordingsSection(
    snackbarHostState: SnackbarHostState,
    onSelectedRecording: suspend (Context, String) -> Unit,
    deleteRecording: suspend (String) -> Unit,
    recordings: List<String>
) {
    if (recordings.isEmpty()) {
        Title(stringResource(R.string.empty_recordings_state_prompt))
    } else {
        Recordings(snackbarHostState, onSelectedRecording, deleteRecording, recordings)
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
internal fun spTextUnit(size: Float) = TextUnit(value = size, type = TextUnitType.Sp)

internal val paddingXLarge = 32.dp
internal val paddingLarge = 16.dp
internal val paddingMedium = 8.dp
internal val paddingSmall = 4.dp
