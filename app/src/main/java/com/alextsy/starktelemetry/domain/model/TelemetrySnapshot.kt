package com.alextsy.starktelemetry.domain.model

import java.time.Instant

data class TelemetrySnapshot(
    val bike: Bike,
    val timestamp: Instant,
    val battery: Battery,
    val motor: Motor,
    val rideSettings: RideSettings,
    val session: RideSession,
    val diagnostics: Diagnostics,
)

data class Bike(
    val model: String,
    val variant: String,
    val firmwareVersion: String,
    val imageUrl: String,
)

data class Battery(
    val stateOfChargePercent: Int,
    val estimatedRangeKm: Int,
    val temperatureCelsius: Double,
    val chargingState: ChargingState,
)

enum class ChargingState {
    CHARGING,
    DISCHARGING,
    FULL,
    UNKNOWN,
}

data class Motor(
    val powerHp: Double,
    val temperatureCelsius: Double,
)

data class RideSettings(
    val powerMap: String,
    val maxPowerHp: Int,
    val engineBrakingPercent: Int,
    val regenerationPercent: Int,
)

data class RideSession(
    val durationSeconds: Long,
    val distanceKm: Double,
    val maxSpeedKmh: Double,
)

data class Diagnostics(
    val faultCodes: List<String>,
    val warnings: List<DiagnosticWarning>,
)

data class DiagnosticWarning(
    val code: String,
    val message: String,
    val severity: WarningSeverity,
)

enum class WarningSeverity {
    INFO,
    WARNING,
    CRITICAL,
    UNKNOWN,
}
