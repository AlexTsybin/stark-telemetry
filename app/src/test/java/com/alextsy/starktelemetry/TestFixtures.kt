package com.alextsy.starktelemetry

import com.alextsy.starktelemetry.domain.model.Battery
import com.alextsy.starktelemetry.domain.model.Bike
import com.alextsy.starktelemetry.domain.model.ChargingState
import com.alextsy.starktelemetry.domain.model.DiagnosticWarning
import com.alextsy.starktelemetry.domain.model.Diagnostics
import com.alextsy.starktelemetry.domain.model.Motor
import com.alextsy.starktelemetry.domain.model.RideSession
import com.alextsy.starktelemetry.domain.model.RideSettings
import com.alextsy.starktelemetry.domain.model.TelemetrySnapshot
import com.alextsy.starktelemetry.domain.model.WarningSeverity
import java.time.Instant

object TestFixtures {
    val validSnapshot = TelemetrySnapshot(
        bike = Bike(
            model = "Stark VARG MX 1.2",
            variant = "Alpha",
            firmwareVersion = "3.4.1",
            imageUrl = "https://example.com/bike.webp",
        ),
        timestamp = Instant.parse("2025-05-19T10:32:45Z"),
        battery = Battery(
            stateOfChargePercent = 73,
            estimatedRangeKm = 38,
            temperatureCelsius = 34.7,
            chargingState = ChargingState.DISCHARGING,
        ),
        motor = Motor(powerHp = 52.4, temperatureCelsius = 61.2),
        rideSettings = RideSettings(
            powerMap = "enduro",
            maxPowerHp = 80,
            engineBrakingPercent = 45,
            regenerationPercent = 60,
        ),
        session = RideSession(
            durationSeconds = 3742,
            distanceKm = 24.7,
            maxSpeedKmh = 94.1,
        ),
        diagnostics = Diagnostics(
            faultCodes = emptyList(),
            warnings = listOf(
                DiagnosticWarning(
                    code = "W_MOT_TEMP_HIGH",
                    message = "Motor temperature elevated",
                    severity = WarningSeverity.WARNING,
                ),
            ),
        ),
    )

    val validJson = """
        {
          "bike": {
            "model": "Stark VARG MX 1.2",
            "variant": "Alpha",
            "firmware_version": "3.4.1",
            "image_url": "https://example.com/bike.webp"
          },
          "timestamp": "2025-05-19T10:32:45Z",
          "battery": {
            "state_of_charge_pct": 73,
            "estimated_range_km": 38,
            "temperature_c": 34.7,
            "charging_state": "discharging"
          },
          "motor": { "power_hp": 52.4, "temperature_c": 61.2 },
          "ride_settings": {
            "power_map": "enduro",
            "max_power_hp": 80,
            "engine_braking_pct": 45,
            "regen_pct": 60
          },
          "session": {
            "duration_s": 3742,
            "distance_km": 24.7,
            "max_speed_kmh": 94.1
          },
          "diagnostics": {
            "fault_codes": [],
            "warnings": [
              {
                "code": "W_MOT_TEMP_HIGH",
                "message": "Motor temperature elevated",
                "severity": "warning"
              }
            ]
          }
        }
    """.trimIndent()
}
