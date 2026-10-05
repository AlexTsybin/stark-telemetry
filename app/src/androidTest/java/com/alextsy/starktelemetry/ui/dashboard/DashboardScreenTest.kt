package com.alextsy.starktelemetry.ui.dashboard

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToIndex
import com.alextsy.starktelemetry.presentation.dashboard.BatteryUiModel
import com.alextsy.starktelemetry.presentation.dashboard.DashboardIntent
import com.alextsy.starktelemetry.presentation.dashboard.DashboardUiModel
import com.alextsy.starktelemetry.presentation.dashboard.DashboardUiState
import com.alextsy.starktelemetry.presentation.dashboard.DiagnosticsUiModel
import com.alextsy.starktelemetry.presentation.dashboard.MotorUiModel
import com.alextsy.starktelemetry.presentation.dashboard.RideSessionUiModel
import com.alextsy.starktelemetry.presentation.dashboard.RideSettingsUiModel
import com.alextsy.starktelemetry.presentation.dashboard.WarningSeverityUi
import com.alextsy.starktelemetry.presentation.dashboard.WarningUiModel
import com.alextsy.starktelemetry.ui.theme.StarkTelemetryTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class DashboardScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun content_displaysAllRequiredTelemetry() {
        composeRule.setContent {
            StarkTelemetryTheme {
                DashboardScreen(
                    state = DashboardUiState.Content(telemetry),
                    onIntent = {},
                )
            }
        }

        assertDisplayed("ALPHA · FW 3.4.1", "Motor temperature elevated")
        assertEquals(
            0,
            composeRule.onAllNodesWithText("SNAPSHOT", substring = true)
                .fetchSemanticsNodes().size,
        )
        composeRule.onNodeWithTag("dashboard").performScrollToIndex(2)
        assertDisplayed("73%", "38 km")

        composeRule.onNodeWithTag("dashboard").performScrollToIndex(3)
        assertDisplayed("24.7 km", "94.1 km/h")

        composeRule.onNodeWithTag("dashboard").performScrollToIndex(4)
        assertDisplayed("52.4 hp")

        composeRule.onNodeWithTag("dashboard").performScrollToIndex(5)
        assertDisplayed("ENDURO")
    }

    @Test
    fun error_retryEmitsRetryIntent() {
        val intents = mutableListOf<DashboardIntent>()
        composeRule.setContent {
            StarkTelemetryTheme {
                DashboardScreen(
                    state = DashboardUiState.Error("Telemetry data could not be read."),
                    onIntent = intents::add,
                )
            }
        }

        composeRule.onNodeWithTag("retry").performClick()

        assertEquals(listOf(DashboardIntent.RetryClicked), intents)
    }

    private fun assertDisplayed(vararg values: String) {
        values.forEach { value -> composeRule.onNodeWithText(value).assertIsDisplayed() }
    }

    private companion object {
        val telemetry = DashboardUiModel(
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
                        message = "Motor temperature elevated",
                        severity = WarningSeverityUi.WARNING,
                    ),
                ),
                faultCount = 0,
            ),
        )
    }
}
