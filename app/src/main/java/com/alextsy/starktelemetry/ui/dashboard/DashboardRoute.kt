package com.alextsy.starktelemetry.ui.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.alextsy.starktelemetry.presentation.dashboard.DashboardViewModel

@Composable
fun DashboardRoute(viewModel: DashboardViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    DashboardScreen(
        state = state,
        onIntent = viewModel::accept,
    )
}
