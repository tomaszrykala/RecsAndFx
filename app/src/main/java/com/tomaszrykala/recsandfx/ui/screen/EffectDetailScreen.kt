package com.tomaszrykala.recsandfx.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import com.tomaszrykala.recsandfx.ShowSnackbar
import com.tomaszrykala.recsandfx.data.NativeInterface
import com.tomaszrykala.recsandfx.data.juceEffects
import com.tomaszrykala.recsandfx.oboeRealFx
import com.tomaszrykala.recsandfx.paddingLarge
import com.tomaszrykala.recsandfx.paddingMedium

@OptIn(ExperimentalUnitApi::class)
@Preview(showBackground = true)
@Composable
fun EffectDetailScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    contentPadding: PaddingValues = PaddingValues(),
    effectName: String = juceEffects[0].name,
) {
    val effect = oboeRealFx.find { it.name == effectName } ?: juceEffects[0] // DEBUG
    var isRecording by rememberSaveable { mutableStateOf(false) }
    var hasRecordingStarted by rememberSaveable { mutableStateOf(false) }

    if (isRecording) {
        ShowSnackbar(snackbarHostState, "You're recording!")
    } else if (hasRecordingStarted) {
        ShowSnackbar(snackbarHostState, "You've stopped recording!")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(contentPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .padding(bottom = paddingMedium)
                .fillMaxWidth(),
            text = effect.name,
            style = TextStyle(
                fontSize = TextUnit(
                    value = 24.0F,
                    type = TextUnitType.Sp,
                ),
                fontWeight = FontWeight.Bold
            )
        )
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
                // SLIDER
                Text(text = param.maxValue.toString())
            }
        }
        IconButton(
            onClick = {
                if (isRecording) {
//                    stopAudioRecorder() // CRASH
                    isRecording = false
                } else {
//                    startAudioRecorder() // CRASH
                    hasRecordingStarted = true
                    isRecording = true
                }
            },
            modifier = Modifier
                .padding(top = 64.dp) // replace with a Spacer
                .background(color = Color.Yellow, shape = RoundedCornerShape(paddingLarge))
                .padding(paddingLarge)
        ) {
            Icon(imageVector = Icons.Default.AddCircle, contentDescription = "")
        }
    }
}

private fun startAudioRecorder() {
    NativeInterface.startAudioRecorder()
}

private fun stopAudioRecorder() {
    NativeInterface.stopAudioRecorder()
}