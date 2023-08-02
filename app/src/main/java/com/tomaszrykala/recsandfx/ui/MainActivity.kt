package com.tomaszrykala.recsandfx.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tomaszrykala.recsandfx.ui.theme.RecsAndFxTheme
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel: RecsAndFxViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RecsAndFxTheme { RecsAndFxScreen(viewModel) } }
        viewModel.onCreated(this@MainActivity)
    }

    override fun onDestroy() = super.onDestroy().also { viewModel.onDestroyed() }
}
