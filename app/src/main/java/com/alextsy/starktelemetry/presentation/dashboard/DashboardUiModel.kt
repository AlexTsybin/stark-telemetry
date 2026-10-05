package com.alextsy.starktelemetry.presentation.dashboard

data class DashboardUiModel(
    val model: String,
    val variantAndFirmware: String,
    val imageUrl: String,
    val updatedAt: String,
    val battery: BatteryUiModel,
    val motor: MotorUiModel,
    val rideSettings: RideSettingsUiModel,
    val session: RideSessionUiModel,
    val diagnostics: DiagnosticsUiModel,
)

data class BatteryUiModel(
    val percentage: String,
    val percentageValue: Float,
    val estimatedRange: String,
    val temperature: String,
    val chargingState: String,
)

data class MotorUiModel(
    val power: String,
    val temperature: String,
)

data class RideSettingsUiModel(
    val powerMap: String,
    val maximumPower: String,
    val engineBraking: String,
    val regeneration: String,
)

data class RideSessionUiModel(
    val duration: String,
    val distance: String,
    val maximumSpeed: String,
)

data class DiagnosticsUiModel(
    val warnings: List<WarningUiModel>,
    val faultCount: Int,
)

data class WarningUiModel(
    val message: String,
    val severity: WarningSeverityUi,
)

enum class WarningSeverityUi {
    INFO,
    WARNING,
    CRITICAL,
    UNKNOWN,
}
