package com.example.alcoholapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import android.view.Window
import androidx.core.view.WindowCompat

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF4CAF50), // Medium Green
    onPrimary = Color.Black,
    primaryContainer = Color(0xFF1B5E20), // Dark Green
    onPrimaryContainer = Color(0xFFC8E6C9), // Very Light Green
    secondary = Color(0xFF81C784), // Light Green
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF2E7D32), // Medium-Dark Green
    onSecondaryContainer = Color(0xFFE8F5E9), // Very Light Green
    tertiary = Color(0xFF00BFA5), // Teal Green
    onTertiary = Color.Black,
    background = Color(0xFF1B2613), // Very Dark Green Background
    onBackground = Color(0xFFE8F5E9), // Very Light Green
    surface = Color(0xFF1B2613), // Very Dark Green Background
    onSurface = Color(0xFFE8F5E9), // Very Light Green
    error = Color(0xFFEF9A9A), // Light Red
    onError = Color.Black
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF1B5E20), // Dark Green
    onPrimary = Color.White,
    primaryContainer = Color(0xFFA5D6A7), // Light Green
    onPrimaryContainer = Color(0xFF0A3D12), // Very Dark Green
    secondary = Color(0xFF2E7D32), // Medium Green
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB9F6CA), // Very Light Green
    onSecondaryContainer = Color(0xFF0A3D12), // Very Dark Green
    tertiary = Color(0xFF004D40), // Dark Teal Green
    onTertiary = Color.White,
    background = Color(0xFFE8F5E9), // Very Light Green Background
    onBackground = Color(0xFF1B1C17), // Almost Black
    surface = Color.White,
    onSurface = Color(0xFF1B1C17), // Almost Black
    error = Color(0xFFB71C1C), // Dark Red
    onError = Color.White
)

@Composable
fun AlcoholAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}