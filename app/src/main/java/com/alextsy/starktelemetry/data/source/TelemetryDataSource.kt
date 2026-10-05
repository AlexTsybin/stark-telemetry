package com.alextsy.starktelemetry.data.source

interface TelemetryDataSource {
    suspend fun readSnapshot(): String
}
