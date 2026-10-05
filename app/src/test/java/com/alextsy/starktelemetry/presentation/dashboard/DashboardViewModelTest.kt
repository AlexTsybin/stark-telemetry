package com.alextsy.starktelemetry.presentation.dashboard

import com.alextsy.starktelemetry.MainDispatcherRule
import com.alextsy.starktelemetry.TestFixtures
import com.alextsy.starktelemetry.domain.repository.TelemetryFailure
import com.alextsy.starktelemetry.domain.repository.TelemetryLoadResult
import com.alextsy.starktelemetry.domain.repository.TelemetryRepository
import com.alextsy.starktelemetry.domain.usecase.LoadTelemetrySnapshotUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val mapper = DashboardUiMapper()

    @Test
    fun `initial load transitions from loading to content`() = runTest {
        val viewModel = viewModelWith(TelemetryLoadResult.Success(TestFixtures.validSnapshot))

        assertEquals(DashboardUiState.Loading, viewModel.state.value)
        advanceUntilIdle()

        val state = viewModel.state.value
        assertTrue(state is DashboardUiState.Content)
        assertEquals("73%", (state as DashboardUiState.Content).telemetry.battery.percentage)
    }

    @Test
    fun `empty result shows empty state`() = runTest {
        val viewModel = viewModelWith(TelemetryLoadResult.Empty)

        advanceUntilIdle()

        assertEquals(DashboardUiState.Empty, viewModel.state.value)
    }

    @Test
    fun `failure result shows a safe error`() = runTest {
        val viewModel = viewModelWith(
            TelemetryLoadResult.Failure(TelemetryFailure.INVALID_DATA),
        )

        advanceUntilIdle()

        assertEquals(
            DashboardUiState.Error("Telemetry data could not be read."),
            viewModel.state.value,
        )
    }

    @Test
    fun `retry can recover from failure to content`() = runTest {
        val repository = SequenceRepository(
            mutableListOf(
                TelemetryLoadResult.Failure(TelemetryFailure.SOURCE_UNAVAILABLE),
                TelemetryLoadResult.Success(TestFixtures.validSnapshot),
            ),
        )
        val viewModel = DashboardViewModel(LoadTelemetrySnapshotUseCase(repository), mapper)
        advanceUntilIdle()
        assertTrue(viewModel.state.value is DashboardUiState.Error)

        viewModel.accept(DashboardIntent.RetryClicked)
        assertEquals(DashboardUiState.Loading, viewModel.state.value)
        advanceUntilIdle()

        assertTrue(viewModel.state.value is DashboardUiState.Content)
        assertEquals(2, repository.calls)
    }

    private fun viewModelWith(result: TelemetryLoadResult): DashboardViewModel {
        return DashboardViewModel(
            LoadTelemetrySnapshotUseCase(SequenceRepository(mutableListOf(result))),
            mapper,
        )
    }

    private class SequenceRepository(
        private val results: MutableList<TelemetryLoadResult>,
    ) : TelemetryRepository {
        var calls: Int = 0
            private set

        override suspend fun loadSnapshot(): TelemetryLoadResult {
            calls += 1
            return results.removeFirst()
        }
    }
}
