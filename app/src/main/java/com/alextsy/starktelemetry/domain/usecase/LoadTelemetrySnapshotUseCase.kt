package com.alextsy.starktelemetry.domain.usecase

import com.alextsy.starktelemetry.domain.model.TelemetrySnapshot
import com.alextsy.starktelemetry.domain.repository.TelemetryFailure
import com.alextsy.starktelemetry.domain.repository.TelemetryLoadResult
import com.alextsy.starktelemetry.domain.repository.TelemetryRepository
import javax.inject.Inject

class LoadTelemetrySnapshotUseCase @Inject constructor(
    private val repository: TelemetryRepository,
) {
    suspend operator fun invoke(): TelemetryLoadResult {
        return when (val result = repository.loadSnapshot()) {
            is TelemetryLoadResult.Success -> validate(result.snapshot)
            TelemetryLoadResult.Empty -> result
            is TelemetryLoadResult.Failure -> result
        }
    }

    private fun validate(snapshot: TelemetrySnapshot): TelemetryLoadResult {
        val valid = with(snapshot) {
            bike.model.isNotBlank() &&
                bike.variant.isNotBlank() &&
                bike.firmwareVersion.isNotBlank() &&
                battery.stateOfChargePercent in PERCENT_RANGE &&
                battery.estimatedRangeKm >= 0 &&
                motor.powerHp >= 0 &&
                rideSettings.maxPowerHp >= 0 &&
                rideSettings.engineBrakingPercent in PERCENT_RANGE &&
                rideSettings.regenerationPercent in PERCENT_RANGE &&
                session.durationSeconds >= 0 &&
                session.distanceKm >= 0 &&
                session.maxSpeedKmh >= 0
        }

        return if (valid) {
            TelemetryLoadResult.Success(snapshot)
        } else {
            TelemetryLoadResult.Failure(TelemetryFailure.INVALID_DATA)
        }
    }

    private companion object {
        val PERCENT_RANGE = 0..100
    }
}
