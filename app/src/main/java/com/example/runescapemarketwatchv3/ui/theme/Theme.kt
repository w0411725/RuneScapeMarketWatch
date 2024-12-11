package com.example.runescapemarketwatchv3.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.graphics.Color


// Define RuneScape-inspired colors
private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF8B4513), // Bronze-like brown
    secondary = Color(0xFF556B2F), // Dark olive green
    tertiary = Color(0xFF4682B4), // Steel blue
    background = Color(0xFF1E1E1E), // Dark gray for a nighttime feel
    surface = Color(0xFF292929), // Slightly lighter gray
    onPrimary = Color(0xFFEDEDED), // Light accent for text on bronze
    onSecondary = Color(0xFFD4AF37), // Gold-like accent for olive green
    onTertiary = Color(0xFFEDEDED), // Light accent for blue elements
    onBackground = Color(0xFFEDEDED), // Light text on dark background
    onSurface = Color(0xFFEDEDED) // Light text on dark surfaces
)

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFD4AF37), // Gold-like yellow
    secondary = Color(0xFF8B4513), // Bronze brown
    tertiary = Color(0xFF2E8B57), // Sea green
    background = Color(0xFF6E4406), // Beige for parchment-like look
    surface = Color(0xFFFAF0E6), // Off-white for surfaces
    onPrimary = Color(0xFF1E1E1E), // Dark text on gold
    onSecondary = Color(0xFF653E10), // Light text on bronze
    onTertiary = Color(0xFF1E1E1E), // Dark text on green
    onBackground = Color(0xFF1E1E1E), // Dark text on light background
    onSurface = Color(0xFF1E1E1E) // Dark text on light surfaces
)

@Composable
fun RuneScapeMarketWatchv3Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
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