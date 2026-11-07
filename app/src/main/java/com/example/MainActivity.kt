package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.ai.configureAppCheck
import com.example.ui.navigation.AppNavigation
import com.example.ui.theme.BackgroundLavender
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AiLearningViewModel
import com.example.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mandatory step for Firebase AI Logic & App Check attestation
        configureAppCheck(this, intent)

        enableEdgeToEdge()

        val mainViewModel: MainViewModel by viewModels()
        val aiViewModel: AiLearningViewModel by viewModels()

        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundLavender
                ) {
                    AppNavigation(
                        mainViewModel = mainViewModel,
                        aiViewModel = aiViewModel
                    )
                }
            }
        }
    }
}
