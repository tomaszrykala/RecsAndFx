package com.tomaszrykala.feature_effects.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tomaszrykala.recsandfx.feature_effects.R

@Composable
@Preview(showBackground = true)
fun EffectsScreen(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    contentPadding: PaddingValues = PaddingValues(),
    navigateToDetail: (effect: String) -> Unit = { },
) {
    var selectedEffect by rememberSaveable { mutableStateOf("") }

    if (selectedEffect != "") {
        ShowSnackbar(
            snackbarHostState, stringResource(R.string.you_ve_selected_effect, selectedEffect)
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(paddingMedium),
    ) {
//        items(emptyList()) { // allEffects
//            Column(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .background(
//                        color = if (selectedEffect == it.name) Color.DarkGray else Color.Green,
//                        shape = RoundedCornerShape(size = 4.dp)
//                    )
//                    .clickable {
//                        selectedEffect = it.name
//                        navigateToDetail.invoke(it.name)
//                    }
//                    .padding(all = paddingMedium)
//            ) {
//                Row(verticalAlignment = Alignment.CenterVertically) {
//                    Icon(
//                        imageVector = it.icon, contentDescription = "",
//                        modifier = Modifier.background(Color.Yellow)
//                    )
//                    Text(
//                        text = it.name,
//                        modifier = Modifier.padding(start = paddingMedium),
//                        style = TextStyle(
//                            fontSize = TextUnit(
//                                value = 24.0F,
//                                type = TextUnitType.Sp
//                            )
//                        )
//                    )
//                }
//                Text(
//                    text = it.info,
//                    modifier = Modifier.padding(top = paddingMedium)
//                )
//            }
//        }
    }
}

@Composable
fun ShowSnackbar(snackbarHostState: SnackbarHostState, message: String) {
    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
    }
}

// TEMP
val paddingMedium = 8.dp
//val allEffects: List<Effect> = NativeInterface.effectDescriptionMap.map {
//    Effect(
//        name = it.key,
//        category = it.value.category,
//        id = it.value.id,
//        icon = Icons.Default.Add,
//        params = it.value.paramValues.map { pd -> pd.toParam() }
//    )
//}