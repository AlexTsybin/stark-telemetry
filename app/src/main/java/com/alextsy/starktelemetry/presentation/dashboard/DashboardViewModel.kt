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

    fun onIntent(intent: DashboardIntent) {
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
                DashboardUiState.Error(DashboardError.UNKNOWN)
            }
        }
    }

    private fun TelemetryLoadResult.toUiState(): DashboardUiState = when (this) {
        is TelemetryLoadResult.Success -> DashboardUiState.Content(uiMapper.map(snapshot))
        TelemetryLoadResult.Empty -> DashboardUiState.Empty
        is TelemetryLoadResult.Failure -> DashboardUiState.Error(reason.toDashboardError())
    }

    private fun TelemetryFailure.toDashboardError(): DashboardError = when (this) {
        TelemetryFailure.SOURCE_UNAVAILABLE -> DashboardError.SOURCE_UNAVAILABLE
        TelemetryFailure.INVALID_DATA -> DashboardError.INVALID_DATA
    }
}
