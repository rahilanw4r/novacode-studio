package com.pocketide.app

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
import com.pocketide.app.ui.MainViewModel
import com.pocketide.app.ui.PocketIDEApp
import com.pocketide.app.ui.screens.splash.PocketSplashScreen
import com.pocketide.app.ui.theme.PocketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val vm: MainViewModel = viewModel()
            val state by vm.state.collectAsStateWithLifecycle()
            var showSplash by rememberSaveable { mutableStateOf(true) }

            PocketTheme(themeMode = state.themeMode) {
                AnimatedContent(
                    targetState = showSplash,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "AppScreenTransition"
                ) { isSplash ->
                    if (isSplash) {
                        PocketSplashScreen(
                            onSplashFinished = { showSplash = false }
                        )
                    } else {
                        PocketIDEApp(vm)
                    }
                }
            }
        }
    }
}
