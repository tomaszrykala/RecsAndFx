package com.tomaszrykala.recsandfx.ui

import android.content.Context
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
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.tomaszrykala.recsandfx.R
import com.tomaszrykala.recsandfx.feature.effect_detail.EffectDetailScreen
import com.tomaszrykala.recsandfx.feature.effects_list.EffectsListScreen
import com.tomaszrykala.recsandfx.feature.permissions.RequestPermissionsScreen
import com.tomaszrykala.recsandfx.ui.screen.Screen
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3Api::class)
fun RecsAndFxScreen(
    windowSizeClass: WindowSizeClass,
    permissionsState: MultiplePermissionsState,
    lifecycleOwner: LifecycleOwner,
    onCreateAction: (c: Context) -> Unit,
    onDestroyAction: () -> Unit,
    onEnableAudioClick: (enable: Boolean) -> Unit,
) {
    OnLifecycleCallbackActions(onCreateAction, onDestroyAction, lifecycleOwner)

    val snackbarHostState = remember { SnackbarHostState() }
    val appName: String = stringResource(R.string.app_name)
    val topBarTitle = rememberSaveable { mutableStateOf(appName) }

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
                title = { Text(topBarTitle.value) },
                actions = { PassthroughButton(snackbarHostState, onEnableAudioClick) }
            )
        }
    ) { contentPadding ->
        if (permissionsState.allPermissionsGranted) {
            ShowRafApp(contentPadding, windowSizeClass, snackbarHostState, topBarTitle)
        } else {
            RequestPermissionsScreen(contentPadding, permissionsState)
        }
    }
}

@Composable
private fun OnLifecycleCallbackActions(
    onCreateAction: (c: Context) -> Unit,
    onDestroyAction: () -> Unit,
    lifecycleOwner: LifecycleOwner
) {
    val currentOnCreate by rememberUpdatedState(onCreateAction)
    val currentOnDestroy by rememberUpdatedState(onDestroyAction)
    val context = LocalContext.current

    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_CREATE) {
                currentOnCreate(context)
            } else if (event == Lifecycle.Event.ON_DESTROY) {
                currentOnDestroy()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }
}

@Composable
private fun PassthroughButton(
    snackbarHostState: SnackbarHostState,
    onEnableAudioClick: (enable: Boolean) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isAudioEnabled by rememberSaveable { mutableStateOf(false) }
    var hasAudioBeenEnabled by rememberSaveable { mutableStateOf(false) }
    val enabledPassthroughMsg = stringResource(R.string.you_ve_enabled_audio_pass_through)
    val disabledPassthroughMsg = stringResource(R.string.you_ve_disabled_audio_pass_through)

    IconButton(onClick = {
        hasAudioBeenEnabled = true
        isAudioEnabled = !isAudioEnabled
        onEnableAudioClick(isAudioEnabled)
        coroutineScope.launch {
            if (isAudioEnabled) {
                snackbarHostState.showSnackbar(enabledPassthroughMsg)
            } else if (hasAudioBeenEnabled) {
                snackbarHostState.showSnackbar(disabledPassthroughMsg)
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
    contentPadding: PaddingValues,
    windowSizeClass: WindowSizeClass,
    snackbarHostState: SnackbarHostState,
    topBarTitle: MutableState<String>
) {
    val appName: String = stringResource(R.string.app_name)
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.EffectsScreen.route) {
        composable(Screen.EffectsScreen.route) {
            topBarTitle.value = appName
            EffectsListScreen(
                snackbarHostState = snackbarHostState,
                contentPadding = contentPadding
            ) { effect -> navController.navigate("detail/${effect}") }
        }
        composable(Screen.EffectDetailScreen.route) {
            val effectName = it.arguments?.getString("effect") ?: "EMPTY"
            topBarTitle.value = "$appName > $effectName"
            EffectDetailScreen(
                snackbarHostState = snackbarHostState,
                windowSizeClass = windowSizeClass,
                contentPadding = contentPadding,
                effectName = effectName
            )
        }
    }
}