package com.alextsy.starktelemetry.presentation.dashboard

import com.alextsy.starktelemetry.TestFixtures
import com.alextsy.starktelemetry.domain.model.ChargingState
import com.alextsy.starktelemetry.domain.model.WarningSeverity
import org.junit.Assert.assertEquals
import org.junit.Test

class DashboardUiMapperTest {
    private val mapper = DashboardUiMapper()

    @Test
    fun `mapper formats telemetry for display`() {
        val result = mapper.map(TestFixtures.validSnapshot)

        assertEquals("73%", result.battery.percentage)
        assertEquals("38 km", result.battery.estimatedRange)
        assertEquals("34.7 °C", result.battery.temperature)
        assertEquals("52.4 hp", result.motor.power)
        assertEquals("ENDURO", result.rideSettings.powerMap)
        assertEquals("1:02:22", result.session.duration)
        assertEquals("24.7 km", result.session.distance)
        assertEquals("May 19, 2025 · 10:32 UTC", result.updatedAt)
    }

    @Test
    fun `mapper labels unknown enum values safely`() {
        val snapshot = TestFixtures.validSnapshot.copy(
            battery = TestFixtures.validSnapshot.battery.copy(
                chargingState = ChargingState.UNKNOWN,
            ),
            diagnostics = TestFixtures.validSnapshot.diagnostics.copy(
                warnings = listOf(
                    TestFixtures.validSnapshot.diagnostics.warnings.single().copy(
                        severity = WarningSeverity.UNKNOWN,
                    ),
                ),
            ),
        )

        val result = mapper.map(snapshot)

        assertEquals("Unknown", result.battery.chargingState)
        assertEquals(WarningSeverityUi.UNKNOWN, result.diagnostics.warnings.single().severity)
    }
}
