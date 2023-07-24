package com.tomaszrykala.recsandfx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.tomaszrykala.recsandfx.ui.theme.RecsAndFxTheme
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.SnackbarDuration
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.ExperimentalUnitApi
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecsAndFxTheme {
                RafApp()
            }
        }
    }
}

val mockEffects = listOf(
    Effect("Delay", Icons.Default.Favorite),
    Effect("Reverb", Icons.Default.Check),
    Effect("Dynamics", Icons.Default.Refresh)
)

data class Effect(val name: String, val icon: ImageVector)

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalUnitApi::class)
fun RafApp() {
    var selectedEffect by rememberSaveable { mutableStateOf("") }
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val snackbarHostState = remember { SnackbarHostState() }
        val paddingLarge = 16.dp
        val paddingMedium = 8.dp

        Scaffold(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingLarge),
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                TopAppBar(
                    title = { Text(stringResource(R.string.app_name)) })
            },
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(padding)
            ) {
                Greeting(name = "tomasz", modifier = Modifier.padding(bottom = paddingMedium))

                if (selectedEffect != "") {
                    ShowSnackBar(
                        snackbarHostState = snackbarHostState,
                        launchKey = selectedEffect
                    )
                }

                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = padding,
                    verticalArrangement = Arrangement.spacedBy(paddingMedium),
                ) {
                    items(mockEffects) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = if (selectedEffect == it.name) Color.DarkGray else Color.Green,
                                    shape = RoundedCornerShape(size = 4.dp)
                                )
                                .clickable {
                                    selectedEffect = if (selectedEffect == "") it.name else ""
                                }
                                .padding(all = paddingMedium),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                imageVector = it.icon, contentDescription = "",
                                modifier = Modifier.background(Color.Yellow)
                            )
                            Text(
                                text = it.name, modifier = Modifier.padding(start = paddingMedium),
                                style = TextStyle(
                                    fontSize = TextUnit(
                                        value = 24.0F,
                                        type = TextUnitType.Sp
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ShowSnackBar(snackbarHostState: SnackbarHostState, launchKey: String) {
    LaunchedEffect(launchKey) {
        snackbarHostState.showSnackbar(
            message = "You've selected $launchKey.",
            duration = SnackbarDuration.Short,
        )
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RecsAndFxTheme {
        RafApp()
    }
}