package com.example.cbamobileapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = ProductivityBlue,
    onPrimary = androidx.compose.ui.graphics.Color.White,

    primaryContainer = ProductivityBlueContainer,
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFF001452),

    secondary = ProductivityGreen,
    onSecondary = androidx.compose.ui.graphics.Color.White,

    background = ProductivityBackground,
    onBackground = androidx.compose.ui.graphics.Color(0xFF1A1B20),

    surface = ProductivitySurface,
    onSurface = androidx.compose.ui.graphics.Color(0xFF1A1B20),

    error = ProductivityError,
    onError = androidx.compose.ui.graphics.Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = ProductivityBlueDark,
    onPrimary = androidx.compose.ui.graphics.Color(0xFF002586),

    primaryContainer = ProductivityBlueContainerDark,
    onPrimaryContainer = androidx.compose.ui.graphics.Color(0xFFDDE1FF),

    secondary = ProductivityGreenDark,
    onSecondary = androidx.compose.ui.graphics.Color(0xFF003824),

    background = ProductivityBackgroundDark,
    onBackground = androidx.compose.ui.graphics.Color(0xFFE3E2E9),

    surface = ProductivitySurfaceDark,
    onSurface = androidx.compose.ui.graphics.Color(0xFFE3E2E9),

    error = ProductivityErrorDark,
    onError = androidx.compose.ui.graphics.Color(0xFF690005)
)

@Composable
fun AiProductivityCoachTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current

    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }

        darkTheme -> DarkColorScheme

        else -> LightColorScheme
    }

    val view = LocalView.current

    if (!view.isInEditMode) {
        val window = (context as Activity).window

        window.statusBarColor =
            colorScheme.background.toArgb()

        WindowCompat.getInsetsController(
            window,
            view
        ).isAppearanceLightStatusBars = !darkTheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}