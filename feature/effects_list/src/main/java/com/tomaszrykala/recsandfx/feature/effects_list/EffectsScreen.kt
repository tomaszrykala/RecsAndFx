package com.tomaszrykala.recsandfx.feature.effects_list

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
@Preview(showBackground = true)
fun EffectsScreen(
    viewModel: EffectsScreenViewModel = koinViewModel(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    contentPadding: PaddingValues = PaddingValues(),
    navigateToDetail: (effect: String) -> Unit = { },
) {
    val coroutineScope = rememberCoroutineScope()
    val state = with(viewModel) {
        coroutineScope.launch { observeEffects() }
        uiStateFlow.collectAsStateWithLifecycle()
    }

    when (state.value) {
        is EffectsScreenUiState.Effects -> ShowEffectsList(
            state.value as EffectsScreenUiState.Effects,
            contentPadding,
            snackbarHostState,
            navigateToDetail
        )

        EffectsScreenUiState.Empty -> {
            ShowSnackbar(snackbarHostState, "Loading...")
        }
    }
}

@Composable
private fun ShowEffectsList(
    effectState: EffectsScreenUiState.Effects,
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState,
    navigateToDetail: (effect: String) -> Unit
) {
    var selectedEffect by rememberSaveable { mutableStateOf("") }

    // Have a different colour for each category? Tint the effect's detail page with the colour?
    val itemSelectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
    val itemUnSelectedColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.6f)

    if (selectedEffect != "") {
        ShowSnackbar(
            snackbarHostState, stringResource(R.string.you_ve_selected_effect, selectedEffect)
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingMedium,
                start = paddingMedium,
                end = paddingMedium
            ),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(paddingMedium),
    ) {
        effectState.effects.forEach { (category, effects) ->
            item { EffectRow(text = category.displayName) }
            items(effects) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = if (selectedEffect == it.name) itemSelectedColor else itemUnSelectedColor,
                            shape = RoundedCornerShape(size = 4.dp)
                        )
                        .clickable {
                            selectedEffect = it.name
                            navigateToDetail.invoke(it.name)
                        }
                        .padding(all = paddingMedium)
                ) {
                    EffectRow(it.icon, it.name)
                    Text(text = it.shortDescription, modifier = Modifier.padding(top = paddingMedium))
                }
            }
        }
    }
}

@Composable
private fun EffectRow(icon: Int? = null, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        icon?.let {
            Icon(
                painterResource(it),
                contentDescription = "Effect icon.",
                modifier = Modifier.background(Color.Yellow)
            )
        }
        Text(
            text = text,
            modifier = Modifier.padding(start = paddingMedium),
            style = TextStyle(
                fontSize = TextUnit(
                    value = 24.0F,
                    type = TextUnitType.Sp
                )
            )
        )
    }
}

@Composable
fun ShowSnackbar(snackbarHostState: SnackbarHostState, message: String) {
    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
    }
}

val paddingMedium = 8.dp