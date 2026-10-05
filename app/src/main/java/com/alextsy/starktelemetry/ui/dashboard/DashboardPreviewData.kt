package com.alextsy.starktelemetry.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.alextsy.starktelemetry.presentation.dashboard.BatteryUiModel
import com.alextsy.starktelemetry.presentation.dashboard.DashboardUiModel
import com.alextsy.starktelemetry.presentation.dashboard.DashboardUiState
import com.alextsy.starktelemetry.presentation.dashboard.DiagnosticsUiModel
import com.alextsy.starktelemetry.presentation.dashboard.MotorUiModel
import com.alextsy.starktelemetry.presentation.dashboard.RideSessionUiModel
import com.alextsy.starktelemetry.presentation.dashboard.RideSettingsUiModel
import com.alextsy.starktelemetry.presentation.dashboard.WarningSeverityUi
import com.alextsy.starktelemetry.presentation.dashboard.WarningUiModel
import com.alextsy.starktelemetry.ui.theme.StarkTelemetryTheme

private val previewTelemetry = DashboardUiModel(
    model = "Stark VARG MX 1.2",
    variantAndFirmware = "ALPHA · FW 3.4.1",
    imageUrl = "",
    updatedAt = "May 19, 2025 · 10:32 UTC",
    battery = BatteryUiModel("73%", 0.73f, "38 km", "34.7 °C", "Discharging"),
    motor = MotorUiModel("52.4 hp", "61.2 °C"),
    rideSettings = RideSettingsUiModel("ENDURO", "80 hp", "45%", "60%"),
    session = RideSessionUiModel("1:02:22", "24.7 km", "94.1 km/h"),
    diagnostics = DiagnosticsUiModel(
        warnings = listOf(
            WarningUiModel(
                code = "W_MOT_TEMP_HIGH",
                message = "Motor temperature elevated",
                severity = WarningSeverityUi.WARNING,
            ),
        ),
        faultCodes = emptyList(),
    ),
)

@Preview(widthDp = 891, heightDp = 411, showBackground = true)
@Composable
private fun ContentPreview() {
    StarkTelemetryTheme {
        DashboardScreen(DashboardUiState.Content(previewTelemetry), onIntent = {})
    }
}

@Preview(widthDp = 891, heightDp = 411, showBackground = true)
@Composable
private fun HealthyContentPreview() {
    StarkTelemetryTheme {
        DashboardScreen(
            DashboardUiState.Content(
                previewTelemetry.copy(
                    diagnostics = DiagnosticsUiModel(emptyList(), emptyList()),
                ),
            ),
            onIntent = {},
        )
    }
}

@Preview(widthDp = 891, heightDp = 411, showBackground = true)
@Composable
private fun LoadingPreview() {
    StarkTelemetryTheme {
        DashboardScreen(DashboardUiState.Loading, onIntent = {})
    }
}

@Preview(widthDp = 891, heightDp = 411, showBackground = true)
@Composable
private fun EmptyPreview() {
    StarkTelemetryTheme {
        DashboardScreen(DashboardUiState.Empty, onIntent = {})
    }
}

@Preview(widthDp = 891, heightDp = 411, showBackground = true)
@Composable
private fun ErrorPreview() {
    StarkTelemetryTheme {
        DashboardScreen(
            DashboardUiState.Error("Telemetry data could not be read."),
            onIntent = {},
        )
    }
}
