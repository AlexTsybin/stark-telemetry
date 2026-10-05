package com.alextsy.starktelemetry

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alextsy.starktelemetry.presentation.dashboard.DashboardViewModel
import com.alextsy.starktelemetry.ui.dashboard.DashboardRoute
import com.alextsy.starktelemetry.ui.theme.StarkTelemetryTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val dashboardViewModel: DashboardViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StarkTelemetryTheme {
                DashboardRoute(viewModel = dashboardViewModel)
            }
        }
    }
}
