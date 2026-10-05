package com.alextsy.starktelemetry.data.parser

import com.alextsy.starktelemetry.data.dto.TelemetryDto
import javax.inject.Inject
import kotlinx.serialization.json.Json

class TelemetryJsonParser @Inject constructor(
    private val json: Json,
) {
    fun parse(rawJson: String): TelemetryDto? {
        if (rawJson.isBlank()) return null
        return json.decodeFromString<TelemetryDto>(rawJson)
    }
}
