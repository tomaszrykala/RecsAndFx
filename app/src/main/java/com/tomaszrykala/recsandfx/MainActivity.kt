package com.tomaszrykala.recsandfx

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.tomaszrykala.recsandfx.data.Effect
import com.tomaszrykala.recsandfx.data.NativeInterface
import com.tomaszrykala.recsandfx.data.toParam
import com.tomaszrykala.recsandfx.ui.screen.EffectsScreen
import com.tomaszrykala.recsandfx.ui.screen.RequestPermissionsScreen
import com.tomaszrykala.recsandfx.ui.screen.Screen
import com.tomaszrykala.recsandfx.ui.screen.effect_detail.EffectDetailScreen
import com.tomaszrykala.recsandfx.ui.screen.getPermissionsList
import com.tomaszrykala.recsandfx.ui.theme.RecsAndFxTheme
import com.tomaszrykala.recsandfx.ui.theme.paddingLarge

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RecsAndFxTheme { RafApp() } }
    }

    // TODO THIS IS A TEMP HACK. Do in composable, repeat on lifecycle
    override fun onPause() {
        NativeInterface.destroyAudioEngine()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO)
            == PackageManager.PERMISSION_GRANTED
        ) {
            NativeInterface.createAudioEngine()
            NativeInterface.enable(true) // isAudioEnabled (repeat on lifecycle?)
        }
    }
}

@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
private fun RafApp() {
    val snackbarHostState = remember { SnackbarHostState() }
    var isAudioEnabled by rememberSaveable { mutableStateOf(false) }
    var hasAudioBeenEnabled by rememberSaveable { mutableStateOf(false) }

    HandleIsAudioEnabledState(isAudioEnabled, snackbarHostState, hasAudioBeenEnabled)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingLarge),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        isAudioEnabled = !isAudioEnabled
                        hasAudioBeenEnabled = true
                        // NativeInterface.enable(isAudioEnabled)
                    }) {
                        Icon(
                            painter = painterResource(
                                if (isAudioEnabled) R.drawable.ic_baseline_audio_is_enabled_24
                                else R.drawable.ic_baseline_audio_is_disabled_24
                            ),
                            contentDescription = null
                        )
                    }
                }
            )
        },
        // floatingActionButton = { FloatingActionButton(onClick = { }) {} }

    ) { contentPadding ->
        val permissionsState = rememberMultiplePermissionsState(getPermissionsList())
        if (permissionsState.allPermissionsGranted) {
            ShowRafApp(snackbarHostState, contentPadding)
        } else {
            RequestPermissionsScreen(contentPadding, permissionsState)
        }
    }
}

@Composable
private fun HandleIsAudioEnabledState(
    isAudioEnabled: Boolean,
    snackbarHostState: SnackbarHostState,
    hasAudioBeenEnabled: Boolean
) {
    if (isAudioEnabled) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.you_ve_enabled_audio_pass_through))
    } else if (hasAudioBeenEnabled) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.you_ve_disabled_audio_pass_through))
    }
    Log.d(TAG, "isAudioEnabled: $isAudioEnabled")
    NativeInterface.enable(isAudioEnabled)
}

@Composable
private fun ShowRafApp(
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
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
                snackbarHostState = snackbarHostState,
                contentPadding = contentPadding,
                effectName = it.arguments?.getString("effect") ?: "EMPTY"
            )
        }
    }
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
