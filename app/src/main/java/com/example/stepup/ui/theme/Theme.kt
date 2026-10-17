package com.example.stepup.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val StepUpColorScheme = lightColorScheme(
    primary = StepUpRed,
    onPrimary = Color.White,
    primaryContainer = StepUpPaleRed,
    onPrimaryContainer = StepUpDarkRed,
    secondary = StepUpDarkRed,
    background = StepUpBackground,
    onBackground = StepUpText,
    surface = Color.White,
    onSurface = StepUpText,
    onSurfaceVariant = StepUpMutedText
)

@Composable
fun StepUpTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = StepUpColorScheme,
        typography = Typography,
        content = content
    )
}
