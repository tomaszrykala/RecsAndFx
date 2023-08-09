package com.tomaszrykala.recsandfx.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.tomaszrykala.recsandfx.RecsAndFxViewModel
import com.tomaszrykala.recsandfx.ui.theme.RecsAndFxTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel: RecsAndFxViewModel by inject()

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RecsAndFxTheme {
                val permissionsState = rememberMultiplePermissionsState(viewModel.getPermissions())
                RecsAndFxScreen(
                    permissionsState,
                    lifecycleOwner = this,
                    onCreateAction = { viewModel.onCreated(this) },
                    onDestroyAction = { viewModel.onDestroyed() },
                ) { isEnabled: Boolean ->
                    viewModel.enableAudio(isEnabled)
                }
            }
        }
    }
}
