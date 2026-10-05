package com.alextsy.starktelemetry.data.mapper

import com.alextsy.starktelemetry.data.dto.TelemetryDto
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
import java.util.Locale

fun TelemetryDto.toDomain(): TelemetrySnapshot = TelemetrySnapshot(
    bike = Bike(
        model = bike.model,
        variant = bike.variant,
        firmwareVersion = bike.firmwareVersion,
        imageUrl = bike.imageUrl,
    ),
    timestamp = Instant.parse(timestamp),
    battery = Battery(
        stateOfChargePercent = battery.stateOfChargePercent,
        estimatedRangeKm = battery.estimatedRangeKm,
        temperatureCelsius = battery.temperatureCelsius,
        chargingState = battery.chargingState.toChargingState(),
    ),
    motor = Motor(
        powerHp = motor.powerHp,
        temperatureCelsius = motor.temperatureCelsius,
    ),
    rideSettings = RideSettings(
        powerMap = rideSettings.powerMap,
        maxPowerHp = rideSettings.maxPowerHp,
        engineBrakingPercent = rideSettings.engineBrakingPercent,
        regenerationPercent = rideSettings.regenerationPercent,
    ),
    session = RideSession(
        durationSeconds = session.durationSeconds,
        distanceKm = session.distanceKm,
        maxSpeedKmh = session.maxSpeedKmh,
    ),
    diagnostics = Diagnostics(
        faultCodes = diagnostics.faultCodes,
        warnings = diagnostics.warnings.map { warning ->
            DiagnosticWarning(
                code = warning.code,
                message = warning.message,
                severity = warning.severity.toWarningSeverity(),
            )
        },
    ),
)

private fun String.toChargingState(): ChargingState = when (lowercase(Locale.ROOT)) {
    "charging" -> ChargingState.CHARGING
    "discharging" -> ChargingState.DISCHARGING
    "full" -> ChargingState.FULL
    else -> ChargingState.UNKNOWN
}

private fun String.toWarningSeverity(): WarningSeverity = when (lowercase(Locale.ROOT)) {
    "info" -> WarningSeverity.INFO
    "warning" -> WarningSeverity.WARNING
    "critical" -> WarningSeverity.CRITICAL
    else -> WarningSeverity.UNKNOWN
}
