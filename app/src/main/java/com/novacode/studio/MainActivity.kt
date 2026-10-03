package com.novacode.studio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.novacode.studio.ui.MainViewModel
import com.novacode.studio.ui.NovaCodeApp
import com.novacode.studio.ui.screens.splash.NovaSplashScreen
import com.novacode.studio.ui.theme.NovaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: MainViewModel = viewModel()
            val state by vm.state.collectAsStateWithLifecycle()
            var showSplash by rememberSaveable { mutableStateOf(true) }

            NovaTheme(themeMode = state.themeMode) {
                AnimatedContent(
                    targetState = showSplash,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "AppScreenTransition"
                ) { isSplash ->
                    if (isSplash) {
                        NovaSplashScreen(
                            onSplashFinished = { showSplash = false }
                        )
                    } else {
                        NovaCodeApp(vm)
                    }
                }
            }
        }
    }
}
