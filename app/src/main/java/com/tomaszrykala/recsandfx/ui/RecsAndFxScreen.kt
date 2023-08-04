package com.tomaszrykala.recsandfx.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.tomaszrykala.recsandfx.R
import com.tomaszrykala.recsandfx.feature.effect_detail.EffectDetailScreen
import com.tomaszrykala.recsandfx.feature.effects_list.EffectsListScreen
import com.tomaszrykala.recsandfx.feature.permissions.RequestPermissionsScreen
import com.tomaszrykala.recsandfx.ui.screen.Screen
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
fun RecsAndFxScreen(
    permissionsState: MultiplePermissionsState = rememberMultiplePermissionsState(emptyList()),
    onEnableAudioClick: (enable: Boolean) -> Unit = {},
) {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                colors = with(MaterialTheme.colorScheme) {
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = primary,
                        titleContentColor = background,
                        actionIconContentColor = background,
                    )
                },
                title = { Text(stringResource(R.string.app_name)) },
                actions = { PassthroughButton(onEnableAudioClick, snackbarHostState) }
            )
        }
    ) { contentPadding ->
        if (permissionsState.allPermissionsGranted) {
            ShowRafApp(snackbarHostState, contentPadding)
        } else {
            RequestPermissionsScreen(contentPadding, permissionsState)
        }
    }
}

@Composable
private fun PassthroughButton(
    onEnableAudioClick: (enable: Boolean) -> Unit,
    snackbarHostState: SnackbarHostState
) {
    val coroutineScope = rememberCoroutineScope()
    var isAudioEnabled by rememberSaveable { mutableStateOf(false) }
    var hasAudioBeenEnabled by rememberSaveable { mutableStateOf(false) }
    val enabledPassthrough = stringResource(R.string.you_ve_enabled_audio_pass_through)
    val disabledPassthrough = stringResource(R.string.you_ve_disabled_audio_pass_through)

    IconButton(onClick = {
        hasAudioBeenEnabled = true
        isAudioEnabled = !isAudioEnabled
        onEnableAudioClick.invoke(isAudioEnabled)
        coroutineScope.launch {
            if (isAudioEnabled) {
                snackbarHostState.showSnackbar(enabledPassthrough)
            } else if (hasAudioBeenEnabled) {
                snackbarHostState.showSnackbar(disabledPassthrough)
            }
        }
    }) {
        Icon(
            painterResource(
                if (isAudioEnabled) R.drawable.ic_round_hearing_enabled_24
                else R.drawable.ic_round_hearing_disabled_24
            ),
            stringResource(R.string.pass_through)
        )
    }
}

@Composable
private fun ShowRafApp(
    snackbarHostState: SnackbarHostState,
    contentPadding: PaddingValues
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.EffectsScreen.route) {
        composable(Screen.EffectsScreen.route) {
            EffectsListScreen(
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