package com.tomaszrykala.recsandfx.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.tomaszrykala.recsandfx.RecsAndFxViewModel
import com.tomaszrykala.recsandfx.ui.theme.RecsAndFxTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel: RecsAndFxViewModel by inject()

    @OptIn(ExperimentalPermissionsApi::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val permissionsState = rememberMultiplePermissionsState(viewModel.getPermissions())

            RecsAndFxTheme {
                RecsAndFxScreen(
                    windowSizeClass,
                    permissionsState,
                    lifecycleOwner = this,
                    onCreateAction = { viewModel.onCreated(this) },
                    onDestroyAction = { viewModel.onDestroyed() },
                ) { isEnabled: Boolean -> viewModel.enableAudio(isEnabled) }
            }
        }
    }
}
