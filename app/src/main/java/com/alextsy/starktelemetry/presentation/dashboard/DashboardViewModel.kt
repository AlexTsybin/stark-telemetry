package com.alextsy.starktelemetry.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alextsy.starktelemetry.domain.repository.TelemetryFailure
import com.alextsy.starktelemetry.domain.repository.TelemetryLoadResult
import com.alextsy.starktelemetry.domain.usecase.LoadTelemetrySnapshotUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val loadTelemetrySnapshot: LoadTelemetrySnapshotUseCase,
    private val uiMapper: DashboardUiMapper,
) : ViewModel() {
    private val mutableState = MutableStateFlow<DashboardUiState>(DashboardUiState.Loading)
    val state: StateFlow<DashboardUiState> = mutableState.asStateFlow()

    init {
        loadTelemetry()
    }

    fun accept(intent: DashboardIntent) {
        when (intent) {
            DashboardIntent.RetryClicked -> loadTelemetry()
        }
    }

    private fun loadTelemetry() {
        mutableState.value = DashboardUiState.Loading
        viewModelScope.launch {
            mutableState.value = try {
                loadTelemetrySnapshot().toUiState()
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                DashboardUiState.Error(UNKNOWN_ERROR_MESSAGE)
            }
        }
    }

    private fun TelemetryLoadResult.toUiState(): DashboardUiState = when (this) {
        is TelemetryLoadResult.Success -> DashboardUiState.Content(uiMapper.map(snapshot))
        TelemetryLoadResult.Empty -> DashboardUiState.Empty
        is TelemetryLoadResult.Failure -> DashboardUiState.Error(reason.message())
    }

    private fun TelemetryFailure.message(): String = when (this) {
        TelemetryFailure.SOURCE_UNAVAILABLE -> "Telemetry snapshot is unavailable."
        TelemetryFailure.INVALID_DATA -> "Telemetry data could not be read."
        TelemetryFailure.UNKNOWN -> UNKNOWN_ERROR_MESSAGE
    }

    private companion object {
        const val UNKNOWN_ERROR_MESSAGE = "Something went wrong while loading telemetry."
    }
}
