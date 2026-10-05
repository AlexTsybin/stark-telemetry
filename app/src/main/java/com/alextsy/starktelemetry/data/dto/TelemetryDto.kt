package com.alextsy.starktelemetry.data.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TelemetryDto(
    val bike: BikeDto,
    val timestamp: String,
    val battery: BatteryDto,
    val motor: MotorDto,
    @SerialName("ride_settings") val rideSettings: RideSettingsDto,
    val session: RideSessionDto,
    val diagnostics: DiagnosticsDto,
)

@Serializable
data class BikeDto(
    val model: String,
    val variant: String,
    @SerialName("firmware_version") val firmwareVersion: String,
    @SerialName("image_url") val imageUrl: String,
)

@Serializable
data class BatteryDto(
    @SerialName("state_of_charge_pct") val stateOfChargePercent: Int,
    @SerialName("estimated_range_km") val estimatedRangeKm: Int,
    @SerialName("temperature_c") val temperatureCelsius: Double,
    @SerialName("charging_state") val chargingState: String,
)

@Serializable
data class MotorDto(
    @SerialName("power_hp") val powerHp: Double,
    @SerialName("temperature_c") val temperatureCelsius: Double,
)

@Serializable
data class RideSettingsDto(
    @SerialName("power_map") val powerMap: String,
    @SerialName("max_power_hp") val maxPowerHp: Int,
    @SerialName("engine_braking_pct") val engineBrakingPercent: Int,
    @SerialName("regen_pct") val regenerationPercent: Int,
)

@Serializable
data class RideSessionDto(
    @SerialName("duration_s") val durationSeconds: Long,
    @SerialName("distance_km") val distanceKm: Double,
    @SerialName("max_speed_kmh") val maxSpeedKmh: Double,
)

@Serializable
data class DiagnosticsDto(
    @SerialName("fault_codes") val faultCodes: List<String>,
    val warnings: List<DiagnosticWarningDto>,
)

@Serializable
data class DiagnosticWarningDto(
    val code: String,
    val message: String,
    val severity: String,
)
