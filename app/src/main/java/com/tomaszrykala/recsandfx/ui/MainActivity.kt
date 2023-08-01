package com.tomaszrykala.recsandfx.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.tomaszrykala.recsandfx.ui.theme.RecsAndFxTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val viewModel: RecsAndFxViewModel by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { RecsAndFxTheme { RecsAndFxScreen(viewModel) } }
    }

    override fun onStart() = super.onStart().also {
        lifecycleScope.launch { viewModel.onStart(this@MainActivity) }
    }

    override fun onStop() = super.onStop().also {
        lifecycleScope.launch { viewModel.onStop() }
    }
}
