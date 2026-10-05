package com.alextsy.starktelemetry.domain.usecase

import com.alextsy.starktelemetry.TestFixtures
import com.alextsy.starktelemetry.domain.model.TelemetrySnapshot
import com.alextsy.starktelemetry.domain.repository.TelemetryFailure
import com.alextsy.starktelemetry.domain.repository.TelemetryLoadResult
import com.alextsy.starktelemetry.domain.repository.TelemetryRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class LoadTelemetrySnapshotUseCaseTest {
    @Test
    fun `valid snapshot passes validation`() = runTest {
        val result = useCaseReturning(TestFixtures.validSnapshot)()

        assertEquals(TelemetryLoadResult.Success(TestFixtures.validSnapshot), result)
    }

    @Test
    fun `invalid domain values return invalid data`() = runTest {
        val valid = TestFixtures.validSnapshot
        val invalidSnapshots = listOf(
            valid.copy(bike = valid.bike.copy(model = "")),
            valid.copy(battery = valid.battery.copy(stateOfChargePercent = -1)),
            valid.copy(battery = valid.battery.copy(stateOfChargePercent = 101)),
            valid.copy(battery = valid.battery.copy(estimatedRangeKm = -1)),
            valid.copy(motor = valid.motor.copy(powerHp = -0.1)),
            valid.copy(rideSettings = valid.rideSettings.copy(maxPowerHp = -1)),
            valid.copy(rideSettings = valid.rideSettings.copy(engineBrakingPercent = 101)),
            valid.copy(rideSettings = valid.rideSettings.copy(regenerationPercent = -1)),
            valid.copy(session = valid.session.copy(durationSeconds = -1)),
            valid.copy(session = valid.session.copy(distanceKm = -0.1)),
            valid.copy(session = valid.session.copy(maxSpeedKmh = -0.1)),
        )

        invalidSnapshots.forEach { snapshot ->
            assertEquals(
                TelemetryLoadResult.Failure(TelemetryFailure.INVALID_DATA),
                useCaseReturning(snapshot)(),
            )
        }
    }

    @Test
    fun `empty and failure results pass through`() = runTest {
        val emptyUseCase = LoadTelemetrySnapshotUseCase(repositoryReturning(TelemetryLoadResult.Empty))
        val failure = TelemetryLoadResult.Failure(TelemetryFailure.SOURCE_UNAVAILABLE)
        val failureUseCase = LoadTelemetrySnapshotUseCase(repositoryReturning(failure))

        assertEquals(TelemetryLoadResult.Empty, emptyUseCase())
        assertEquals(failure, failureUseCase())
    }

    private fun useCaseReturning(snapshot: TelemetrySnapshot) =
        LoadTelemetrySnapshotUseCase(
            repositoryReturning(TelemetryLoadResult.Success(snapshot)),
        )

    private fun repositoryReturning(result: TelemetryLoadResult) =
        object : TelemetryRepository {
            override suspend fun loadSnapshot(): TelemetryLoadResult = result
        }
}
