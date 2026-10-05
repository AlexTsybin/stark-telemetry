package com.alextsy.starktelemetry.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val StarkColorScheme = lightColorScheme(
    primary = StarkRed,
    onPrimary = TelemetryCard,
    secondary = CockpitNavy,
    onSecondary = TelemetryCard,
    background = TelemetrySurface,
    onBackground = TelemetryText,
    surface = TelemetryCard,
    onSurface = TelemetryText,
    surfaceVariant = TelemetrySurface,
    onSurfaceVariant = TelemetryMuted,
    error = StarkRed,
)

@Composable
fun StarkTelemetryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = StarkColorScheme,
        typography = StarkTypography,
        content = content,
    )
}
