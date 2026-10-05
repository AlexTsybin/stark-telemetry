package com.alextsy.starktelemetry.presentation.dashboard

sealed interface DashboardIntent {
    data object RetryClicked : DashboardIntent
}

sealed interface DashboardUiState {
    data object Loading : DashboardUiState
    data class Content(val telemetry: DashboardUiModel) : DashboardUiState
    data object Empty : DashboardUiState
    data class Error(val message: String) : DashboardUiState
}
