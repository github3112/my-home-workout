package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = WorkoutOrange,
    onPrimary = Color.Black,
    primaryContainer = WorkoutPurpleWall,
    onPrimaryContainer = WorkoutOrangeLight,
    secondary = WorkoutOrangeLight,
    onSecondary = Color.Black,
    tertiary = FitnessGreen,
    onTertiary = Color.Black,
    background = WorkoutSplashBg,
    onBackground = TextPrimaryDark,
    surface = WorkoutDarkCard,
    onSurface = TextPrimaryDark,
    surfaceVariant = WorkoutDarkSurface,
    onSurfaceVariant = TextSecondaryDark,
    outline = WorkoutDarkStroke
)

private val LightColorScheme = lightColorScheme(
    primary = WorkoutOrangeDark,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFECE0),
    onPrimaryContainer = Color(0xFF6B3300),
    secondary = WorkoutPurpleWall,
    onSecondary = Color.White,
    tertiary = FitnessGreen,
    onTertiary = Color.White,
    background = WorkoutLightBg,
    onBackground = TextPrimaryLight,
    surface = WorkoutLightCard,
    onSurface = TextPrimaryLight,
    surfaceVariant = Color(0xFFEEF2F6),
    onSurfaceVariant = TextSecondaryLight,
    outline = WorkoutLightStroke
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to sleek workout dark theme
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

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
