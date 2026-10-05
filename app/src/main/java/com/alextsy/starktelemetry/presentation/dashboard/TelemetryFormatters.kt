package com.alextsy.starktelemetry.presentation.dashboard

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

object TelemetryFormatters {
    private val timestampFormatter = DateTimeFormatter
        .ofPattern("MMM d, yyyy · HH:mm 'UTC'", Locale.US)
        .withZone(ZoneOffset.UTC)

    fun duration(totalSeconds: Long): String {
        val hours = totalSeconds / 3_600
        val minutes = (totalSeconds % 3_600) / 60
        val seconds = totalSeconds % 60
        return String.format(Locale.US, "%d:%02d:%02d", hours, minutes, seconds)
    }

    fun decimal(value: Double): String = String.format(Locale.US, "%.1f", value)

    fun timestamp(instant: Instant): String = timestampFormatter.format(instant)
}
