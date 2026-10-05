package com.alextsy.starktelemetry.presentation.dashboard

import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Test

class TelemetryFormattersTest {
    @Test
    fun `duration formats hours minutes and seconds`() {
        assertEquals("1:02:22", TelemetryFormatters.duration(3742))
        assertEquals("0:00:00", TelemetryFormatters.duration(0))
    }

    @Test
    fun `decimal uses one locale-independent decimal place`() {
        assertEquals("24.7", TelemetryFormatters.decimal(24.7))
        assertEquals("80.0", TelemetryFormatters.decimal(80.0))
    }

    @Test
    fun `timestamp is rendered in UTC`() {
        assertEquals(
            "May 19, 2025 · 10:32 UTC",
            TelemetryFormatters.timestamp(Instant.parse("2025-05-19T10:32:45Z")),
        )
    }
}
