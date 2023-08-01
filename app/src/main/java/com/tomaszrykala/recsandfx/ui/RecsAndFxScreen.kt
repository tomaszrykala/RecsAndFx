package com.tomaszrykala.recsandfx.ui

import android.util.Log
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.tomaszrykala.recsandfx.R
import com.tomaszrykala.recsandfx.TAG
import com.tomaszrykala.recsandfx.feature.effect_detail.EffectDetailScreen
import com.tomaszrykala.recsandfx.feature.effects_list.EffectsScreen
import com.tomaszrykala.recsandfx.feature.permissions.RequestPermissionsScreen
import com.tomaszrykala.recsandfx.feature.permissions.getPermissionsList
import com.tomaszrykala.recsandfx.ui.screen.Screen
import org.koin.androidx.compose.koinViewModel

@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
fun RecsAndFxScreen(
    viewModel: RecsAndFxViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    var isAudioEnabled by rememberSaveable { mutableStateOf(false) }
    var hasAudioBeenEnabled by rememberSaveable { mutableStateOf(false) }

    HandlePassthroughState(snackbarHostState, isAudioEnabled, hasAudioBeenEnabled, viewModel)

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = paddingLarge),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                actions = {
                    IconButton(onClick = {
                        isAudioEnabled = !isAudioEnabled
                        hasAudioBeenEnabled = true
                    }) {
                        Icon(
                            painter = painterResource(
                                if (isAudioEnabled) R.drawable.ic_round_hearing_enabled_24
                                else R.drawable.ic_round_hearing_disabled_24
                            ),
                            contentDescription = "Pass-through"
                        )
                    }
                }
            )
        }
    ) { contentPadding ->
        val permissionsState = rememberMultiplePermissionsState(getPermissionsList())
        if (permissionsState.allPermissionsGranted) {
            ShowRafApp(snackbarHostState, contentPadding)
        } else {
            RequestPermissionsScreen(
                contentPadding,
                permissionsState
            )
        }
    }
}

@Composable
private fun HandlePassthroughState(
    snackbarHostState: SnackbarHostState,
    isAudioEnabled: Boolean,
    hasAudioBeenEnabled: Boolean,
    viewModel: RecsAndFxViewModel
) {
    if (isAudioEnabled) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.you_ve_enabled_audio_pass_through))
    } else if (hasAudioBeenEnabled) {
        ShowSnackbar(snackbarHostState, stringResource(R.string.you_ve_disabled_audio_pass_through))
    }
    Log.d(TAG, "isAudioEnabled: $isAudioEnabled")
    viewModel.enableAudio(isAudioEnabled)
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
                snackbarHostState = snackbarHostState,
                contentPadding = contentPadding
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

val paddingLarge = 16.dp