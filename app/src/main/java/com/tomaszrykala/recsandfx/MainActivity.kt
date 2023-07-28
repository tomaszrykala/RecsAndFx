package com.tomaszrykala.recsandfx

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.tomaszrykala.recsandfx.data.Effect
import com.tomaszrykala.recsandfx.data.NativeInterface
import com.tomaszrykala.recsandfx.data.toParam
import com.tomaszrykala.recsandfx.ui.screen.EffectDetailScreen
import com.tomaszrykala.recsandfx.ui.screen.EffectsScreen
import com.tomaszrykala.recsandfx.ui.theme.RecsAndFxTheme

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecsAndFxTheme {
                val snackbarHostState = remember { SnackbarHostState() }

                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingLarge),
                    snackbarHost = { SnackbarHost(snackbarHostState) },
                    topBar = { TopAppBar(title = { Text(stringResource(R.string.app_name)) }) },
                    // floatingActionButton = { FloatingActionButton(onClick = { }) {} }
                ) { contentPadding ->
                    RafApp(
                        contentPadding,
                        snackbarHostState,
                    )
                }
            }
        }
    }
}

val paddingLarge = 16.dp
val paddingMedium = 8.dp

@Composable
fun RafApp(
    contentPadding: PaddingValues,
    snackbarHostState: SnackbarHostState
) {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.EffectsScreen.route) {
        composable(Screen.EffectsScreen.route) {
            EffectsScreen(
                snackbarHostState,
                contentPadding
            ) { effect -> navController.navigate("detail/${effect}") }
        }
        composable(Screen.EffectDetailScreen.route) {
            EffectDetailScreen(
                snackbarHostState,
                contentPadding,
                it.arguments?.getString("effect") ?: "EMPTY"
            )
        }
    }
}

sealed class Screen(val route: String) {
    object EffectsScreen : Screen("list")
    object EffectDetailScreen : Screen("detail/{effect}")
}

@Composable
fun ShowSnackbar(snackbarHostState: SnackbarHostState, message: String) {
    LaunchedEffect(message) {
        snackbarHostState.showSnackbar(message, duration = SnackbarDuration.Short)
    }
}

val oboeRealFx: List<Effect> = NativeInterface.effectDescriptionMap.map {
    Effect(
        it.key, Icons.Default.Add, params = it.value.paramValues.map { pd -> pd.toParam() }
    )
}
