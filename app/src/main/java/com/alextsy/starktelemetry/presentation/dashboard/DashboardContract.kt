package com.alextsy.starktelemetry.presentation.dashboard

sealed interface DashboardIntent {
    data object RetryClicked : DashboardIntent
}

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Content(val telemetry: DashboardUiModel) : DashboardUiState
    data object Empty : DashboardUiState
    data class Error(val error: DashboardError) : DashboardUiState
}

enum class DashboardError {
    SOURCE_UNAVAILABLE,
    INVALID_DATA,
    UNKNOWN,
}
