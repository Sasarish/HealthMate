package com.example.fitnessapp.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable


private val BlackWhiteColorScheme = lightColorScheme(
    primary = AppBlack,
    onPrimary = AppWhite,
    primaryContainer = LightGrey,
    onPrimaryContainer = AppBlack,
    secondary = AppBlack,
    onSecondary = AppWhite,
    secondaryContainer = LightGrey,
    onSecondaryContainer = AppBlack,
    tertiary = AppBlack,
    onTertiary = AppWhite,
    background = AppWhite,
    onBackground = AppBlack,
    surface = AppWhite,
    onSurface = AppBlack,
    surfaceVariant = LightGrey,
    onSurfaceVariant = MidGrey,
    surfaceTint = AppWhite,
    outline = BorderGrey,
    outlineVariant = BorderGrey,
    surfaceContainerLowest = AppWhite,
    surfaceContainerLow = AppWhite,
    surfaceContainer = AppWhite,
    surfaceContainerHigh = AppWhite,
    surfaceContainerHighest = LightGrey
)

// Using same colour to every screen
@Composable
fun FitnessAppTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = BlackWhiteColorScheme,
        typography = Typography,
        content = content
    )
}