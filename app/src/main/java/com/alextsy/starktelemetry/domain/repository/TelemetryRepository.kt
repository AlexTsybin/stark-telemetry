package com.alextsy.starktelemetry.domain.repository

import com.alextsy.starktelemetry.domain.model.TelemetrySnapshot

interface TelemetryRepository {
    suspend fun loadSnapshot(): TelemetryLoadResult
}

sealed interface TelemetryLoadResult {
    data class Success(val snapshot: TelemetrySnapshot) : TelemetryLoadResult
    data object Empty : TelemetryLoadResult
    data class Failure(val reason: TelemetryFailure) : TelemetryLoadResult
}

enum class TelemetryFailure {
    SOURCE_UNAVAILABLE,
    INVALID_DATA,
    UNKNOWN,
}
