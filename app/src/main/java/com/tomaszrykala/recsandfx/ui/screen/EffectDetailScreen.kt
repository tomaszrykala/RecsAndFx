package com.tomaszrykala.recsandfx.ui.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import com.tomaszrykala.recsandfx.data.juceEffects

@OptIn(ExperimentalUnitApi::class)
@Preview(showBackground = true)
@Composable
fun EffectDetailScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    contentPadding: PaddingValues = PaddingValues(),
    effect: String = juceEffects[0].name,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .padding(contentPadding),
        color = MaterialTheme.colorScheme.background,
    ) {
        Text(
            text = effect,
            modifier = Modifier.fillMaxWidth(),
            style = TextStyle(
                fontSize = TextUnit(
                    value = 24.0F,
                    type = TextUnitType.Sp
                )
            )
        )
    }
}