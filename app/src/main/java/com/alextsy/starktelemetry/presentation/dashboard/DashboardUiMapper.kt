package com.alextsy.starktelemetry.presentation.dashboard

import com.alextsy.starktelemetry.domain.model.ChargingState
import com.alextsy.starktelemetry.domain.model.TelemetrySnapshot
import com.alextsy.starktelemetry.domain.model.WarningSeverity
import java.util.Locale
import javax.inject.Inject

class DashboardUiMapper @Inject constructor() {
    fun map(snapshot: TelemetrySnapshot): DashboardUiModel = with(snapshot) {
        DashboardUiModel(
            model = bike.model,
            variantAndFirmware = "${bike.variant.uppercase(Locale.ROOT)} · FW ${bike.firmwareVersion}",
            imageUrl = bike.imageUrl,
            updatedAt = TelemetryFormatters.timestamp(timestamp),
            battery = BatteryUiModel(
                percentage = "${battery.stateOfChargePercent}%",
                percentageValue = battery.stateOfChargePercent / 100f,
                estimatedRange = "${battery.estimatedRangeKm} km",
                temperature = "${TelemetryFormatters.decimal(battery.temperatureCelsius)} °C",
                chargingState = battery.chargingState.label(),
            ),
            motor = MotorUiModel(
                power = "${TelemetryFormatters.decimal(motor.powerHp)} hp",
                temperature = "${TelemetryFormatters.decimal(motor.temperatureCelsius)} °C",
            ),
            rideSettings = RideSettingsUiModel(
                powerMap = rideSettings.powerMap.uppercase(Locale.ROOT),
                maximumPower = "${rideSettings.maxPowerHp} hp",
                engineBraking = "${rideSettings.engineBrakingPercent}%",
                regeneration = "${rideSettings.regenerationPercent}%",
            ),
            session = RideSessionUiModel(
                duration = TelemetryFormatters.duration(session.durationSeconds),
                distance = "${TelemetryFormatters.decimal(session.distanceKm)} km",
                maximumSpeed = "${TelemetryFormatters.decimal(session.maxSpeedKmh)} km/h",
            ),
            diagnostics = DiagnosticsUiModel(
                warnings = diagnostics.warnings.map { warning ->
                    WarningUiModel(
                        message = warning.message,
                        severity = warning.severity.toUiSeverity(),
                    )
                },
                faultCount = diagnostics.faultCodes.size,
            ),
        )
    }

    private fun ChargingState.label(): String = when (this) {
        ChargingState.CHARGING -> "Charging"
        ChargingState.DISCHARGING -> "Discharging"
        ChargingState.FULL -> "Full"
        ChargingState.UNKNOWN -> "Unknown"
    }

    private fun WarningSeverity.toUiSeverity(): WarningSeverityUi = when (this) {
        WarningSeverity.INFO -> WarningSeverityUi.INFO
        WarningSeverity.WARNING -> WarningSeverityUi.WARNING
        WarningSeverity.CRITICAL -> WarningSeverityUi.CRITICAL
        WarningSeverity.UNKNOWN -> WarningSeverityUi.UNKNOWN
    }
}
