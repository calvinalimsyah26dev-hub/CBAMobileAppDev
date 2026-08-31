package com.example.cbamobileapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.cbamobileapp.pages.ProductivityCoachApp
import com.example.cbamobileapp.ui.theme.AiProductivityCoachTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(
        savedInstanceState: Bundle?
    ) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AiProductivityCoachTheme {
                ProductivityCoachApp()
            }
        }
    }
}