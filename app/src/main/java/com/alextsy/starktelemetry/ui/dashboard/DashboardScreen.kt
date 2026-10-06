package com.alextsy.starktelemetry.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Inbox
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.alextsy.starktelemetry.R
import com.alextsy.starktelemetry.presentation.dashboard.DashboardError
import com.alextsy.starktelemetry.presentation.dashboard.DashboardIntent
import com.alextsy.starktelemetry.presentation.dashboard.DashboardUiModel
import com.alextsy.starktelemetry.presentation.dashboard.DashboardUiState
import com.alextsy.starktelemetry.ui.theme.CockpitNavy
import com.alextsy.starktelemetry.ui.theme.TelemetrySurface

@Composable
fun DashboardScreen(
    state: DashboardUiState,
    onIntent: (DashboardIntent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(TelemetrySurface)
            .windowInsetsPadding(WindowInsets.safeDrawing),
    ) {
        when (state) {
            DashboardUiState.Loading -> LoadingDashboard()
            DashboardUiState.Empty -> StateMessage(
                icon = Icons.Outlined.Inbox,
                title = "No telemetry available",
                message = "The bike has not produced telemetry data yet.",
                onRetry = { onIntent(DashboardIntent.RetryClicked) },
            )
            is DashboardUiState.Error -> StateMessage(
                icon = Icons.Outlined.ErrorOutline,
                title = "Unable to load telemetry",
                message = state.error.message(),
                onRetry = { onIntent(DashboardIntent.RetryClicked) },
            )
            is DashboardUiState.Content -> DashboardContent(state.telemetry)
        }
    }
}

@Composable
private fun LoadingDashboard() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CockpitNavy)
            .testTag("loading"),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        Spacer(Modifier.height(20.dp))
        Text(
            text = "LOADING BIKE TELEMETRY",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSecondary,
        )
    }
}

@Composable
private fun StateMessage(
    icon: ImageVector,
    title: String,
    message: String,
    onRetry: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(42.dp),
            tint = MaterialTheme.colorScheme.primary,
        )
        Spacer(Modifier.height(14.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(6.dp))
        Text(
            text = message,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onRetry,
            modifier = Modifier
                .height(48.dp)
                .testTag("retry"),
        ) {
            Icon(Icons.Outlined.Refresh, contentDescription = null)
            Spacer(Modifier.width(8.dp))
            Text("Retry")
        }
    }
}

@Composable
private fun DashboardContent(telemetry: DashboardUiModel) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize(),
    ) {
        val columnCount = when {
            maxWidth >= 840.dp -> 4
            maxWidth >= 600.dp -> 2
            else -> 1
        }
        val compactHero = maxWidth < 600.dp

        LazyVerticalGrid(
            columns = GridCells.Fixed(columnCount),
            modifier = Modifier
                .fillMaxSize()
                .testTag("dashboard"),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item(span = { GridItemSpan(maxLineSpan) }) {
                BikeHero(
                    model = telemetry.model,
                    variantAndFirmware = telemetry.variantAndFirmware,
                    updatedAt = telemetry.updatedAt,
                    imageUrl = telemetry.imageUrl,
                    compact = compactHero,
                )
            }

            if (telemetry.diagnostics.warnings.isNotEmpty() || telemetry.diagnostics.faultCount > 0) {
                item(span = { GridItemSpan(maxLineSpan) }) {
                    DiagnosticsPanel(diagnostics = telemetry.diagnostics)
                }
            }

            item {
                BatteryCard(telemetry.battery)
            }

            item {
                MetricCard(
                    icon = Icons.Outlined.Speed,
                    title = "SESSION",
                    primaryValue = telemetry.session.distance,
                    primaryLabel = "DISTANCE",
                    metrics = listOf(
                        "DURATION" to telemetry.session.duration,
                        "MAX SPEED" to telemetry.session.maximumSpeed,
                    ),
                )
            }

            item {
                MetricCard(
                    icon = Icons.Outlined.Bolt,
                    title = "MOTOR",
                    primaryValue = telemetry.motor.power,
                    primaryLabel = "POWER",
                    metrics = listOf("TEMPERATURE" to telemetry.motor.temperature),
                )
            }

            item {
                MetricCard(
                    icon = Icons.Outlined.Tune,
                    title = "RIDE SETTINGS",
                    primaryValue = telemetry.rideSettings.powerMap,
                    primaryLabel = "POWER MAP",
                    metrics = listOf(
                        "MAX POWER" to telemetry.rideSettings.maximumPower,
                        "ENGINE BRAKING" to telemetry.rideSettings.engineBraking,
                        "REGEN" to telemetry.rideSettings.regeneration,
                    ),
                )
            }
        }
    }
}

@Composable
private fun DashboardError.message(): String = when (this) {
    DashboardError.SOURCE_UNAVAILABLE -> stringResource(R.string.telemetry_error_source_unavailable)
    DashboardError.INVALID_DATA -> stringResource(R.string.telemetry_error_invalid_data)
    DashboardError.UNKNOWN -> stringResource(R.string.telemetry_error_unknown)
}
